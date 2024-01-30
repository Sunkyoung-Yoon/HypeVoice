package hypevoice.hypevoiceback.voice.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.excption.VoiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoiceService {

    private final VoiceFindService voiceFindService;

    @Transactional
    public void updateVoice(Long memberId, Long voiceId, String name, String imageUrl, String intro, String email, String phone, String addInfo) {
        validateMember(voiceId, memberId);
        Voice voiceUpdate = voiceFindService.findById(voiceId);

        voiceUpdate.updateVoice(name, imageUrl, intro, email, phone, addInfo);
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

