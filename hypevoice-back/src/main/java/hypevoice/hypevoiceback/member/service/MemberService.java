package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.member.domain.Role;
import hypevoice.hypevoiceback.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberFindService memberFindService;
    private final MemberRepository memberRepository;

    @Transactional
    public void update(Long memberId, String nickname, String profileUrl) {
        Member member = memberFindService.findById(memberId);
        validateDuplicateNickname(nickname);
        member.update(nickname, profileUrl);
    }

    @Transactional
    public void updateRole(Long memberId) {
        Member member = memberFindService.findById(memberId);
        member.updateRole(Role.USER);
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw BaseException.type(MemberErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
