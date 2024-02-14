package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.file.service.FileService;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.studio.OpenViduClient;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.domain.StudioRepository;
import hypevoice.hypevoiceback.studio.dto.StudioJoinResponse;
import hypevoice.hypevoiceback.studio.dto.StudioMessageRequest;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.domain.StudioMemberRepository;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberFindService;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberService;
import io.openvidu.java.client.Recording;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
@Transactional
@RequiredArgsConstructor
public class StudioService {

    private final StudioRepository studioRepository;
    private final StudioFindService studioFindService;
    private final MemberFindService memberFindService;
    private final StudioMemberRepository studioMemberRepository;
    private final StudioMemberService studioMemberService;
    private final StudioMemberFindService studioMemberFindService;
    private final FileService fileService;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final OpenViduClient openViduClient;
    private String sessionId;

    public StudioJoinResponse createStudio(Long loginId, StudioRequest studioRequest) {

        if (studioMemberRepository.existsById(loginId)) {
            throw new BaseException(StudioErrorCode.STUDIO_ALREADY_JOINED);
        }

        if (studioRequest.isPublic() == 0 && studioRequest.password() == null)
            throw new BaseException(StudioErrorCode.NULL_PASSWORD_OF_STUDIO_OR_REQUEST);
        else if (studioRequest.isPublic() == 1 && studioRequest.password() != null)
            throw new BaseException(StudioErrorCode.UNABLE_CONNECT_PRIVATE_ROOM);

        sessionId = openViduClient.createSession();
        System.out.println(sessionId);

        Member member = memberFindService.findById(loginId);
        String password = studioRequest.password();

        if (studioRequest.password() != null) {
            password = bCryptPasswordEncoder.encode(studioRequest.password());
        }
        Studio studio = Studio.createStudio(sessionId, studioRequest.title(),
                studioRequest.intro(), studioRequest.limitNumber(), studioRequest.isPublic(),
                password);

        Map<String, String> map = openViduClient.getJoinStudioToken(sessionId, member.getNickname());
        String token = map.get("token");
        String connectionName = map.get("serverData");

        System.out.println(token + " / " + connectionName);
        studioRepository.save(studio);
        studioMemberService.create(member.getId(), studio.getId(), 1, connectionName);
        // 스튜디오를 생성하면 스튜디오멤버 리스트에도 방장의 권한으로 정보를 추가한다.

        return new StudioJoinResponse(studio.getId(), token);

    }

    public void updateStudio(Long loginId, Long studioId, StudioRequest studioRequest) {
        if (studioRequest.isPublic() == 0 && studioRequest.password() != null)
            throw new BaseException(StudioErrorCode.UNABLE_CONNECT_PRIVATE_ROOM);
        else if (studioRequest.isPublic() == 1 && studioRequest.password() == null)
            throw new BaseException(StudioErrorCode.NULL_PASSWORD_OF_STUDIO_OR_REQUEST);

        Studio studio = studioFindService.findById(studioId);
        Member member = memberFindService.findById(loginId);
        // 방장이 아니면 예외발생
        if (studioMemberFindService.findByMemberIdAndStudioId(member.getId(), studio.getId()).getIsHost() == 0)
            throw BaseException.type(StudioErrorCode.UNABLE_TO_UPDATE_STUDIO);

        String password = studioRequest.password();

        if (studioRequest.password() != null) {
            password = bCryptPasswordEncoder.encode(studioRequest.password());
        }

        studio.updateStudio(studioRequest.title(), studioRequest.intro(), studioRequest.limitNumber(),
                studioRequest.isPublic(), password);
    }


    public void deleteStudio(Long loginId, Long studioId) {
        Studio studio = studioFindService.findById(studioId);
        Member member = memberFindService.findById(loginId);
        sessionId = studio.getSessionId();
        // 방장이 아니면 예외발생
        if (studioMemberFindService.findByMemberIdAndStudioId(member.getId(), studio.getId()).getIsHost() == 0) {
            leaveStudio(loginId, studioId);
            return;
        }
        openViduClient.deleteSession(sessionId);
        System.out.println("deleteSession.ok");
        studioMemberService.delete(studioId);
        System.out.println("studioMem ok");
        studioRepository.deleteById(studio.getId());
        System.out.println("repo delete ok");
    }

    public StudioResponse findOneStudio(Long loginId, Long studioId) {
        Studio studio = studioFindService.findById(studioId);
        memberFindService.findById(loginId);
        return StudioResponse.builder().
                studioId(studio.getId()).
                title(studio.getTitle()).
                intro(studio.getIntro()).
                memberCount(studio.getMemberCount()).
                limitNumber(studio.getLimitNumber()).
                isPublic(studio.getIsPublic()).
                sessionId(studio.getSessionId()).
                onAir(studio.getOnAir()).
                build();
    }

    // 스튜디오 방 입장
    public StudioJoinResponse joinStudio(Long loginId, Long studioId, String password) {
        Member member = memberFindService.findById(loginId);
        Studio studio = studioFindService.findById(studioId);
        Long memberId = member.getId();
        sessionId = studio.getSessionId();
        System.out.println("스튜디오 : " + studio.getIntro() + " / 이름 : " + sessionId);
        System.out.println("멤버 : " + member.getId() + " / 아이디 : " + memberId);
        String token;
        String connectionName;
        if (studioMemberRepository.existsById(loginId)) {
            throw new BaseException(StudioErrorCode.STUDIO_ALREADY_JOINED);
        }
        if (studio.getOnAir() == 1) {
            throw new BaseException(StudioErrorCode.STUDIO_ONAIR);
        }
        if (password == null ^ studio.getPassword() == null) {
            throw BaseException.type(StudioErrorCode.NULL_PASSWORD_OF_STUDIO_OR_REQUEST);
        } else {
            if (password == null) {
                if (studio.getIsPublic() == 0) {
                    throw BaseException.type(StudioErrorCode.UNABLE_CONNECT_PRIVATE_ROOM);
                }
                // 아니면 통과
            } else {
                if (!bCryptPasswordEncoder.matches(password, studio.getPassword())) {
                    throw BaseException.type(StudioErrorCode.INCORRECT_PASSWORD);
                }
                // 아니면 통과
            }
        }
        Map<String, String> map = openViduClient.getJoinStudioToken(sessionId, member.getNickname());
        token = map.get("token");
        connectionName = map.get("serverData");
        studioMemberService.create(member.getId(), studio.getId(), 0, connectionName);

        return new StudioJoinResponse(studio.getId(), token);

    }

    public String screenShare(Long loginId, Long studioId) {
        Member member = memberFindService.findById(loginId);
        Studio studio = studioFindService.findById(studioId);

        return openViduClient.getScreenShareToken(studio.getSessionId(), member.getNickname());


    }

    public void leaveStudio(Long loginId, Long studioId) {
        Member member = memberFindService.findById(loginId);
        Studio studio = studioFindService.findById(studioId);

        StudioMember studioMember = studioMemberFindService.findByMemberIdAndStudioId(loginId, studioId);
        openViduClient.disConnect(studioMember.getConnectionId());
        studioMemberService.delete(loginId, studioId);


    }

    public List<String> studioJoinList(String sessionId) {
        return openViduClient.studioJoinList(sessionId);
    }

    public List<StudioResponse> findAll(final String word, Integer page) {
        int limit = 10;
        int offset = (page - 1) * limit;
        List<Studio> studioList = studioRepository.findAllByTitleOrIntro(PageRequest.of(offset, limit), word).getContent();

        List<StudioResponse> studioResponseList = new ArrayList<>();
        for (Studio s : studioList) {
            studioResponseList.add(new StudioResponse(s.getId(), s.getSessionId(), s.getTitle(), s.getIntro(), s.getMemberCount(), s.getLimitNumber(), s.getIsPublic(), s.getOnAir()));
        }
        return studioResponseList;
    }

    public String startRecording(Long studioId, Long loginId, boolean isIndividual) {
        Studio studio = studioFindService.findById(studioId);
        String sessionId = studio.getSessionId();
        if (studioMemberFindService.findByMemberIdAndStudioId(loginId, studioId).getIsHost() == 1) {
            studio.updateOnAir();
            Recording recording = openViduClient.startRecording(sessionId, isIndividual);

            String id = recording.getId();
            System.out.println(recording.toString());
            return id;
        } else {
            throw new BaseException(StudioErrorCode.UNABLE_RECORDING_START);
        }
    }

    public String stopRecording(String recordingId, Long loginId, Long studioId) {
        Studio studio = studioFindService.findById(studioId);
        if (studioMemberFindService.findByMemberIdAndStudioId(loginId, studioId).getIsHost() == 1) {
            studio.updateOnAir();
            String status = openViduClient.stopRecording(recordingId).getStatus().name();
            return status;
        } else {
            throw new BaseException(StudioErrorCode.UNABLE_RECORDING_STOP);
        }

    }

    public List<String> getRecordingUrl(String recordingId, Long loginId, Long studioId) {
        Studio studio = studioFindService.findById(studioId);
        Recording recording = null;
        if (studioMemberFindService.findByMemberIdAndStudioId(loginId, studioId).getIsHost() == 1) {
            recording = openViduClient.getRecording(recordingId);
            String status = recording.getStatus().name();
            if (status.equals("ready")) {
                return uploadZipFromUrl(recording.getUrl());
            } else {
                throw new BaseException(StudioErrorCode.UNABLE_RECORDING);
            }
        } else {
            throw new BaseException(StudioErrorCode.RECORDING_NOT_FOUND);
        }

    }

    public void deleteRecording(String recordingId, Long loginId, Long studioId) {
        Studio studio = studioFindService.findById(studioId);
        if (studioMemberFindService.findByMemberIdAndStudioId(loginId, studioId).getIsHost() == 1) {
            openViduClient.deleteRecording(recordingId);

        } else {
            throw new BaseException(StudioErrorCode.UNABLE_RECORDING);
        }
    }

    public void message(Long loginId, Long studioId, StudioMessageRequest studioMessageRequest) {
        if (studioMemberRepository.existsByMemberIdAndStudioId(loginId, studioId)) {

            openViduClient.message(studioMessageRequest.sessionId(), studioMessageRequest.sender(), studioMessageRequest.message());
        } else {
            throw new BaseException(StudioErrorCode.STUDIO_MEMBER_NOT_FOUND);
        }


    }

    public List<String> uploadZipFromUrl(String zipFileUrl) {
        List<String> list = new ArrayList<>();
        if(ExtensionFromUrl(zipFileUrl).equals("webm")){
            list.add(zipFileUrl);
        }
        try {
            // Zip 파일 다운로드
            URL url = new URL(zipFileUrl);
            try (InputStream in = url.openStream()) {
                // Zip 파일 압축 해제
                try (ZipInputStream zipIn = new ZipInputStream(in)) {
                    ZipEntry entry = zipIn.getNextEntry();
                    while (entry != null) {
                        String fileName = entry.getName();
                        // 압축 해제된 각 파일을 MultipartFile로 변환하여 S3에 업로드
                        String s = fileService.uploadBoardFiles(convertToMultipartFile(zipIn, fileName, fileName));
                        if (ExtensionFromUrl(s).equals("json")) {
                            zipIn.closeEntry();
                            entry = zipIn.getNextEntry();
                            continue;
                        }
                        list.add(s);
                        zipIn.closeEntry();
                        entry = zipIn.getNextEntry();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
        return list;
    }

    private MultipartFile convertToMultipartFile(InputStream inputStream, String name, String originalFilename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        return new MockMultipartFile(name, originalFilename, null, bytes);
    }

    public String ExtensionFromUrl(String url) {
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1];
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }
        return ""; // 확장자가 없는 경우
    }


}
