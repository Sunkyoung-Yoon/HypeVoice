package hypevoice.hypevoiceback.voice.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.voice.dto.VoiceCard;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoiceFindService {

    private final VoiceRepository voiceRepository;

    public Voice findById(Long voiceId){
        return voiceRepository.findById(voiceId)
                .orElseThrow(() -> BaseException.type(VoiceErrorCode.VOICE_NOT_FOUND));
    }

    public List<VoiceCard> findByKeyword(String keyword){
        return voiceRepository.findByKeyword(keyword)
                .orElseThrow(() -> BaseException.type(VoiceErrorCode.KEYWORD_NOT_FOUND));
    }

}
