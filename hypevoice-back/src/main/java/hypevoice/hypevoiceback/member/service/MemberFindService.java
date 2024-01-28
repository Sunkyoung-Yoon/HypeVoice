package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.member.domain.SocialType;
import hypevoice.hypevoiceback.member.exception.MemberErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberFindService {
    private final MemberRepository memberRepository;

    public Member findBySocialTypeAndEmail(SocialType socialType, String email) {
        return memberRepository.findBySocialTypeAndEmail(socialType, email)
                .orElseThrow(() -> BaseException.type(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> BaseException.type(MemberErrorCode.MEMBER_NOT_FOUND));
    }
}
