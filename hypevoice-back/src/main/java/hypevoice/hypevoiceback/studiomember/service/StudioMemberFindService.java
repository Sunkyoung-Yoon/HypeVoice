package hypevoice.hypevoiceback.studiomember.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import hypevoice.hypevoiceback.studiomember.domain.StudioMember;
import hypevoice.hypevoiceback.studiomember.domain.StudioMemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StudioMemberFindService {

    private final StudioMemberRepository studioMemberRepository;

    public StudioMember findById(Long studioMemberId){
        return studioMemberRepository.findById(studioMemberId).orElseThrow(() -> BaseException.type(StudioErrorCode.STUDIO_MEMBER_NOT_FOUND));
    }


}
