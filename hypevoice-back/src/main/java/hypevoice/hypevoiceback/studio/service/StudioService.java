package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.domain.StudioRepository;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudioService {

    private final StudioRepository studioRepository;
    private final StudioFindService studioFindService;
    private String sessionId;

    public Long createSession(StudioRequest studioRequest) {

        sessionId = "session1";

        Studio studio = Studio.createStudio(sessionId, studioRequest.title(),
                studioRequest.intro(), studioRequest.limitNumber(), studioRequest.isPublic(),
                studioRequest.password());
        studioRepository.save(studio);
        return studio.getId();

    }

    public void updateStudio(Long studioId, StudioRequest studioRequest) {
        Studio studio = studioFindService.findById(studioId);
        studio.updateStudio(studioRequest.title(), studioRequest.intro(), studioRequest.limitNumber(),
                studioRequest.isPublic(), studioRequest.password());
    }


    public void deleteStudio(Long loginId, Long studioId) {
        // 스튜디오 멤버 중 방장만 삭제 가능하게끔 추후 구현
        studioRepository.deleteById(studioId);

    }

    public StudioResponse findOneStudio(Long studioId) {
        Studio studio = studioFindService.findById(studioId);
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
