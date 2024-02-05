package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.domain.StudioRepository;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberFindService;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudioService {

    private final StudioRepository studioRepository;
    private final StudioFindService studioFindService;
    private final MemberFindService memberFindService;
    private final StudioMemberService studioMemberService;
    private final StudioMemberFindService studioMemberFindService;
    private String sessionId;

    public Long createStudio(Long loginId, StudioRequest studioRequest) {
        sessionId = "session1";
        Member member = memberFindService.findById(loginId);
        Studio studio = Studio.createStudio(sessionId, studioRequest.title(),
                studioRequest.intro(), studioRequest.limitNumber(), studioRequest.isPublic(),
                studioRequest.password());

        // 스튜디오를 생성하면 스튜디오멤버 리스트에도 방장의 권한으로 정보를 추가한다.
        studioRepository.save(studio);
        studioMemberService.create(member.getId(), studio.getId(), 1);

        return studio.getId();

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

        // 방장이 아니면 예외발생
        if (studioMemberFindService.findByMemberIdAndStudioId(member.getId(), studio.getId()).getIsHost() == 0)
            throw BaseException.type(StudioErrorCode.UNABLE_TO_DELETE_STUDIO);
        studioMemberService.delete(loginId,studioId);
        studioRepository.deleteById(studioId);

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

}
