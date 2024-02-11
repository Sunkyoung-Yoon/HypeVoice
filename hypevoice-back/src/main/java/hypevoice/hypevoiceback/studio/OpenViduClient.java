package hypevoice.hypevoiceback.studio;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import io.openvidu.java.client.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@RequiredArgsConstructor
@Component
public class OpenViduClient {

    @Value("${OPENVIDU_URL}")
    private String OPENVIDU_URL;
    @Value("${OPENVIDU_SECRET}")
    private String SECRET;
    private final HttpHeaders headers = new HttpHeaders();
    private String sessionId;
    private OpenVidu openVidu;
    private RecordingProperties recordingProperties = new RecordingProperties.Builder()
            .outputMode(Recording.OutputMode.COMPOSED)
            .hasAudio(true)
            .hasVideo(false)
            .name(UUID.randomUUID().toString())

            .build();

    @PostConstruct
    public void init() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(("OPENVIDUAPP:" + SECRET).getBytes()));
    }


    public String createSession() {
        openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
        sessionId = UUID.randomUUID().toString();
        SessionProperties properties = new SessionProperties.Builder()
                .recordingMode(RecordingMode.MANUAL)
                .defaultRecordingProperties(recordingProperties)
                .customSessionId(sessionId)
                .build();
        try {
            Session session = openVidu.createSession(properties);
            return session.getSessionId();
        } catch (OpenViduException e) {
            e.printStackTrace();
            return "세션 생성에 실패했습니다.";
        }
    }

    public void deleteSession(String sessionId) {
        try {
            String url = "/openvidu/api/sessions/" + sessionId;

            WebClient.create().mutate()
                    .baseUrl(OPENVIDU_URL)
                    .build()
                    .delete()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Void.class);


        } catch (Exception e) {
            throw BaseException.type(StudioErrorCode.STUDIO_NOT_FOUND);
        }
    }

    public Map<String, String> getJoinStudioToken(String sessionId, Long memberId) {
        HashMap<String, String> map = new HashMap<>();
        try {
            String url = OPENVIDU_URL + "/openvidu/api/sessions/" + sessionId + "/connection";

            String requestBody = "{" +
                    "\"type\": \"WEBRTC\", " +
                    "\"data\": \"" + memberId + "\", " +
                    "\"role\": \"PUBLISHER\", " +
                    "\"kurentoOptions\": {" +
                    "\"videoMaxRecvBandwidth\": 1000," +
                    "\"videoMinRecvBandwidth\": 300," +
                    "\"videoMaxSendBandwidth\": 1000," +
                    "\"videoMinSendBandwidth\": 300," +
                    "\"allowedFilters\": [ \"GStreamerFilter\", \"ZBarFilter\" ]" +
                    "}," +
                    "\"customIceServers\": [" +
                    "{" +
                    "\"url\": \"turn:turn-domain.com:443\"," +
                    "\"username\": \"usertest\"," +
                    "\"credential\": \"userpass\"" +
                    "}" +
                    "]" +
                    "}";
            System.out.println("RequestBody : " + requestBody);
            String responseBody = WebClient.create().post()
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("ResponseBody : " + responseBody);
            map.put("token", parseToken(responseBody));
            map.put("id", parseConnectionId(responseBody));
        } catch (Exception e) {

            throw BaseException.type(StudioErrorCode.STUDIO_NOT_FOUND);
        }
        return map;
    }

    public void disConnect(String connectionId) {
        try {
            String url = "/openvidu/api/sessions/" + sessionId + "/connection/" + connectionId;

            WebClient.create().mutate()
                    .baseUrl(OPENVIDU_URL)
                    .build()
                    .delete()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Void.class);

        } catch (Exception e) {
            throw BaseException.type(StudioErrorCode.STUDIO_NOT_FOUND);
        }
    }

    private String parseToken(String body) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String token = jsonNode.get("token").asText();

        return token;
    }

    private String parseConnectionId(String body) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = mapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String id = jsonNode.get("id").asText();

        return id;
    }

    private String getString(String url) {
        String body = WebClient.create(OPENVIDU_URL)
                .get()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return body;
    }

    public List<String> studioJoinList(String sessionId) {
        String url = "/openvidu/api/sessions/" + sessionId;
        String body = getString(url);

        System.out.println(body);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        int cnt = jsonNode.get("connections").get("numberOfElements").asInt();
        List<String> username = new ArrayList<>();
        for (int i = 0; i < cnt; i++) {
            username.add(jsonNode.get("connections").get("content").get(i).get("serverData").asText());
        }
        return username;
    }

    public Recording startRecording(String sessionId, Boolean isIndividual) {
        if(isIndividual){
            recordingProperties = new RecordingProperties.Builder()
                    .outputMode(Recording.OutputMode.INDIVIDUAL)
                    .hasAudio(true)
                    .hasVideo(false)
                    .name(UUID.randomUUID().toString())

                    .build();
        }
        try {
            return openVidu.startRecording(sessionId, recordingProperties);
        } catch (Exception e) {
            throw new BaseException(StudioErrorCode.UNABLE_RECORDING_START);
        }
    }

    public Recording stopRecording(String recordingId) {
        Recording recording = null;
        try {
            recording = openVidu.stopRecording(recordingId);
            System.out.println(recording.getId());
            System.out.println("status : " + recording.getStatus());
            return recording;
        } catch (Exception e) {
            throw new BaseException(StudioErrorCode.UNABLE_RECORDING_STOP);
        }
    }

    public Recording getRecording(String recordingId) {
        Recording recording = null;
        try {
            recording = openVidu.getRecording(recordingId);;
            return recording;
        } catch (Exception e) {
            throw new BaseException(StudioErrorCode.RECORDING_NOT_FOUND);
        }
    }

    public void deleteRecording(String recordingId) {
        try {
            openVidu.deleteRecording(recordingId);
        } catch (Exception e) {
            throw new BaseException(StudioErrorCode.UNABLE_RECORDING_DELETE);
        }

    }
}