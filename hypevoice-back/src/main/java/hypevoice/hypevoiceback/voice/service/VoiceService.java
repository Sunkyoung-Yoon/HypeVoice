package hypevoice.hypevoiceback.voice.service;

import hypevoice.hypevoiceback.file.service.FileService;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoiceService {

    private final VoiceFindService voiceFindService;

    private final MemberFindService memberFindService;

    private final VoiceRepository voiceRepository;

    private final FileService fileService;

    @Transactional
    public void createVoice(Long memberId, String name) {
        Member member = memberFindService.findById(memberId);
        Voice voice = Voice.createVoice(member, name);
        voiceRepository.save(voice);
    }

    @Transactional
    public void updateVoice(Long memberId, Long voiceId, String name, String intro, String email,
                            String phone, String addInfo, MultipartFile file) {
        validateMember(voiceId, memberId);
        Voice voiceUpdate = voiceFindService.findById(voiceId);

        String profileImageUrl = null;
        if (file != null)
            profileImageUrl = fileService.uploadVoiceFiles(file);

        if(voiceUpdate.getImageUrl() != null)
            fileService.deleteFiles(voiceUpdate.getImageUrl());

        voiceUpdate.updateVoice(name, profileImageUrl, intro, email, phone, addInfo);
    }

    @Transactional
    public VoiceReadResponse readDetailVoice(Long voiceId) {
        Voice voiceDetail = voiceFindService.findById(voiceId);

        return VoiceReadResponse.builder()
                .name(voiceDetail.getName())
                .imageUrl(voiceDetail.getImageUrl())
                .intro(voiceDetail.getIntro())
                .addInfo(voiceDetail.getAddInfo())
                .email(voiceDetail.getEmail())
                .phone(voiceDetail.getPhone())
                .likes(voiceDetail.getLikes())
                .build();
    }

    private void validateMember(Long voiceId, Long memberId) {
        Voice voice = voiceFindService.findById(voiceId);
        if (!voice.getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER);
        }
    }
}

