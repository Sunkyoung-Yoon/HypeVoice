package hypevoice.hypevoiceback.studiomember.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studio.service.StudioFindService;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.domain.StudioMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StudioMemberService {

    private final StudioMemberRepository studioMemberRepository;
    private final StudioMemberFindService studioMemberFindService;
    private final StudioFindService studioFindService;
    private final MemberFindService memberFindService;

    public void create(Long loginId, Long studioId, int isHost) {
        if (studioMemberRepository.existsByMemberIdAndStudioId(loginId, studioId)) {
            throw BaseException.type(StudioErrorCode.STUDIO_ALREADY_JOINED);
        }
        Studio studio = studioFindService.findById(studioId);
        Member member = memberFindService.findById(loginId);
        StudioMember studioMember = StudioMember.createStudioMember(member, studio, isHost);
        studioMemberRepository.save(studioMember);

    }

    public void delete(Long loginId, Long studioId) {
        if (!studioMemberRepository.existsByMemberIdAndStudioId(loginId, studioId)) {
            throw BaseException.type(StudioErrorCode.STUDIO_MEMBER_NOT_FOUND);
        }
        studioMemberRepository.deleteByMemberIdAndStudioId(loginId, studioId);
    }
}



