package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import hypevoice.hypevoiceback.work.dto.WorkList;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkFindService {
    private final WorkRepository workRepository;

    @Transactional
    public Work findById(Long workId) {
        return workRepository.findById(workId)
                .orElseThrow(() -> BaseException.type(WorkErrorCode.WORK_NOT_FOUND));
    }

    @Transactional
    public Work findRepWorkByVoiceId(Long voiceId) {
        return workRepository.findRepWorkByVoiceId(voiceId);
    }

    @Transactional
    public List<WorkList> findAllByVoiceId(Long voiceId) {

        return workRepository.findAllByVoiceId(voiceId)
                .orElseThrow(() -> BaseException.type(VoiceErrorCode.VOICE_NOT_FOUND));
    }
}
