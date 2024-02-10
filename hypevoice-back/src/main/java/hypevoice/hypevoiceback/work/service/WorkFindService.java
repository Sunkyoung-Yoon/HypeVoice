package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
