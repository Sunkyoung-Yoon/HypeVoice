package hypevoice.hypevoiceback.member.service;

import hypevoice.hypevoiceback.auth.service.AuthService;
import hypevoice.hypevoiceback.file.service.FileService;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.member.domain.Role;
import hypevoice.hypevoiceback.member.dto.MemberResponse;
import hypevoice.hypevoiceback.member.exception.MemberErrorCode;
import hypevoice.hypevoiceback.voice.service.VoiceFindService;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberFindService memberFindService;
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final FileService fileService;
    private final VoiceService voiceService;
    private final VoiceFindService voiceFindService;

    @Transactional
    public void update(Long memberId, String nickname, MultipartFile file) {
        Member member = memberFindService.findById(memberId);
        validateDuplicateNickname(nickname);

        String profileUrl = null;
        if (file != null)
            profileUrl = fileService.uploadMemberFiles(file);

        if(member.getProfileUrl() != null)
            fileService.deleteFiles(member.getProfileUrl());

        member.update(nickname, profileUrl);
    }

    @Transactional
    public void updateRole(Long memberId) {
        Member member = memberFindService.findById(memberId);
        member.updateRole(Role.USER);
    }

    @Transactional
    public MemberResponse read(Long memberId) {
        Member readMember = memberFindService.findById(memberId);
        return MemberResponse.builder()
                .memberId(readMember.getId())
                .username(readMember.getUsername())
                .nickname(readMember.getNickname())
                .email(readMember.getEmail())
                .profileUrl(readMember.getProfileUrl())
                .build();
    }

    @Transactional
    public void delete(Long memberId) {
        Member member = memberFindService.findById(memberId);

        authService.logout(member.getId());

        if(member.getProfileUrl() != null)
            fileService.deleteFiles(member.getProfileUrl());

        voiceService.delete(member.getId());
        memberRepository.delete(member);
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw BaseException.type(MemberErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
