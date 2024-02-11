package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.studio.OpenViduClient;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.domain.StudioRepository;
import hypevoice.hypevoiceback.studio.dto.StudioJoinResponse;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberFindService;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberService;
import io.openvidu.java.client.Recording;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class StudioService {

    private final StudioRepository studioRepository;
    private final StudioFindService studioFindService;
    private final MemberFindService memberFindService;
    private final StudioMemberService studioMemberService;
    private final StudioMemberFindService studioMemberFindService;

    private final OpenViduClient openViduClient;
    private String sessionId;

    public StudioJoinResponse createStudio(Long loginId, StudioRequest studioRequest) {

        sessionId = openViduClient.createSession();

        System.out.println(sessionId);

        Member member = memberFindService.findById(loginId);
        Studio studio = Studio.createStudio(sessionId, studioRequest.title(),
                studioRequest.intro(), studioRequest.limitNumber(), studioRequest.isPublic(),
                studioRequest.password());

        Map<String, String> map = openViduClient.getJoinStudioToken(sessionId, loginId);
        String token = map.get("token");
        String connectionId = map.get("id");

        System.out.println(token + " / " + connectionId);
        studioRepository.save(studio);
        studioMemberService.create(member.getId(), studio.getId(), 1, connectionId);
        // 스튜디오를 생성하면 스튜디오멤버 리스트에도 방장의 권한으로 정보를 추가한다.

        return new StudioJoinResponse(studio.getId(), token);

    }

    public void updateStudio(Long loginId, Long studioId, StudioRequest studioRequest) {
        Studio studio = studioFindService.findById(studioId);
        Member member = memberFindService.findById(loginId);
        // 방장이 아니면 예외발생
        if (studioMemberFindService.findByMemberIdAndStudioId(member.getId(), studio.getId()).getIsHost() == 0)
            throw BaseException.type(StudioErrorCode.UNABLE_TO_UPDATE_STUDIO);
        studio.updateStudio(studioRequest.title(), studioRequest.intro(), studioRequest.limitNumber(),
                studioRequest.isPublic(), studioRequest.password());
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
        String connectionId;
        //
        if (password == null ^ studio.getPassword() == null) {
            throw BaseException.type(StudioErrorCode.NULL_PASSWORD_OF_STUDIO_OR_REQUEST);
        } else {
            if (password == null) {
                if (studio.getIsPublic() == 0) {
                    throw BaseException.type(StudioErrorCode.UNABLE_CONNECT_PRIVATE_ROOM);
                }
                // 아니면 통과
            } else {
                if (!password.equals(studio.getPassword())) {
                    throw BaseException.type(StudioErrorCode.INCORRECT_PASSWORD);
                }
                // 아니면 통과
            }
        }
        Map<String, String> map = openViduClient.getJoinStudioToken(sessionId, memberId);
        token = map.get("token");
        connectionId = map.get("id");
        studioMemberService.create(member.getId(), studio.getId(), 0, connectionId);

        return new StudioJoinResponse(studio.getId(), token);

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

    public String getRecordingUrl(String recordingId, Long loginId, Long studioId) {
        Studio studio = studioFindService.findById(studioId);
        Recording recording = null;
        if (studioMemberFindService.findByMemberIdAndStudioId(loginId, studioId).getIsHost() == 1) {
            recording = openViduClient.getRecording(recordingId);
            String status = recording.getStatus().name();
            if (status.equals("ready")) {
                return recording.getUrl();
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
}
