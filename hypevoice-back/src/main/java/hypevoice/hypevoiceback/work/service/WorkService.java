package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.voice.service.VoiceFindService;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import hypevoice.hypevoiceback.work.dto.WorkResponse;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
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
        validateVoice(voiceId, workId);

        Work work = workFindService.findById(workId);

        work.updateWork(title, videoLink, photoUrl, scriptUrl, recordUrl, info, isRep);
    }

    @Transactional
    public void deleteWork(Long memberId, Long voiceId, Long workId) {
        validateMember(voiceId, memberId);

        workRepository.deleteById(workId);
    }

    @Transactional
    public WorkResponse readWork(Long voiceId, Long workId) {
        validateVoice(voiceId, workId);
        Work work = workFindService.findById(workId);

        return WorkResponse.builder()
                .voiceId(voiceId)
                .workId(workId)
                .title(work.getTitle())
                .videoLink(work.getVideoLink())
                .photoUrl(work.getPhotoUrl())
                .scriptUrl(work.getScriptUrl())
                .recordUrl(work.getRecordUrl())
                .info(work.getInfo())
                .isRep(work.getIsRep())
                .build();
    }


    // 대본 클릭시
    @Transactional
    public String readScriptUrl(Long voiceId, Long workId) {
        validateVoice(voiceId, workId);
        Work work = workFindService.findById(workId);

        return work.getScriptUrl();
    }

    // 유튜브 링크 클릭시
    @Transactional
    public String readVideoLink(Long voiceId, Long workId) {
        validateVoice(voiceId, workId);
        Work work = workFindService.findById(workId);

        return work.getVideoLink();
    }

    // 대표 작업물 설정
    @Transactional
    public void updateRepresentationWork(Long memberId, Long voiceId, Long workId) {
        validateMember(voiceId, memberId);
        validateVoice(voiceId, workId);

        Work work = workFindService.findById(workId);

        work.updateRep();
    }

    private void validateMember(Long voiceId, Long memberId) {
        Voice voice = voiceFindService.findById(voiceId);
        if (!voice.getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER);
        }
    }

    private void validateVoice(Long voiceId, Long workId) {
        Work work = workFindService.findById(workId);
        if (!work.getVoice().getId().equals(voiceId)) {
            throw BaseException.type(WorkErrorCode.WORK_NOT_IN_VOICE);
        }
    }

}
