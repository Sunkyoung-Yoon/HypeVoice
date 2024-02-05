package hypevoice.hypevoiceback.studiomember.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.dto.MemberResponse;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studio.service.StudioFindService;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.domain.StudioMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<MemberResponse> findAllByStudioId(Long studioId) {
        List<Member> memberList = studioMemberRepository.findAllByStudioId(studioId);
        List<MemberResponse> memberResponseList = new ArrayList<>();
        for (Member m : memberList) {
            memberResponseList.add(MemberResponse.builder()
                    .memberId(m.getId())
                    .username(m.getUsername())
                    .nickname(m.getNickname())
                    .email(m.getEmail())
                    .profileUrl(m.getProfileUrl())
                    .build());

        }
        return memberResponseList;
    }

    public StudioResponse findByMemberId(Long memberId) {
        Studio studio = studioMemberRepository.findByMemberId(memberId);
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



