package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.voice.service.VoiceFindService;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkService {
    private final WorkRepository workRepository;
    private final WorkFindService workFindService;
    private final VoiceFindService voiceFindService;

    @Transactional
    public void registerWork(Long memberId, Long voiceId, String title, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info, int isRep) {
        validateMember(voiceId, memberId);

        Voice voice = voiceFindService.findById(voiceId);
        Work work = Work.createWork(voice, title, videoLink, photoUrl, scriptUrl, recordUrl, info, isRep);

        workRepository.save(work);
    }

    @Transactional
    public void updateWork(Long memberId, Long voiceId, Long workId, String title, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info, int isRep) {
        validateMember(voiceId, memberId);

        Work work = workFindService.findById(workId);

        work.updateWork(title, videoLink, photoUrl, scriptUrl, recordUrl, info, isRep);
    }

    @Transactional
    public void deleteWork(Long memberId, Long voiceId, Long workId) {
        validateMember(voiceId, memberId);
        workRepository.deleteById(workId);
    }

    private void validateMember(Long voiceId, Long memberId) {
        Voice voice = voiceFindService.findById(voiceId);
        if (!voice.getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER);
        }
    }

}
