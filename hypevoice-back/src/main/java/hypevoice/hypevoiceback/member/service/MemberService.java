package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberFindService memberFindService;

    @Transactional
    public void updateNickname(Long userId, String value) {
        Member member = memberFindService.findById(userId);
        member.updateNickname(value);
    }
    @Transactional
    public void updateRole(Long userId) {
        Member member = memberFindService.findById(userId);
        member.updateRole(Role.USER);
    }

}
