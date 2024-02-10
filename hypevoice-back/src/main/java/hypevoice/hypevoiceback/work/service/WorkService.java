package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.file.service.FileService;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkService {
    private final WorkRepository workRepository;
    private final WorkFindService workFindService;
    private final VoiceFindService voiceFindService;
    private final FileService fileService;

    @Transactional
    public void registerWork(Long memberId, Long voiceId, String title, String videoLink, String info, int isRep, MultipartFile[] multipartFiles) {
        validateMember(voiceId, memberId);

        Voice voice = voiceFindService.findById(voiceId);
        String[] fileUrlList = new String[3];
        if(multipartFiles != null){
            for(int i=0; i<multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                String fileUrl = null;
                if (file != null)
                    fileUrl = fileService.uploadWorkFiles(file);

                fileUrlList[i] = fileUrl;
            }
        }

        Work work = Work.createWork(voice, title, videoLink, fileUrlList[0], fileUrlList[1], fileUrlList[2], info, isRep);
        workRepository.save(work);
    }

    @Transactional
    public void updateWork(Long memberId, Long voiceId, Long workId, String title, String videoLink, String info, int isRep, MultipartFile[] multipartFiles) {
        validateMember(voiceId, memberId);
        validateVoice(voiceId, workId);

        Work work = workFindService.findById(workId);

        String[] fileUrlList = new String[3];
        if(multipartFiles != null){
            for(int i=0; i<multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                String fileUrl = null;
                if (file != null)
                    fileUrl = fileService.uploadWorkFiles(file);

                fileUrlList[i] = fileUrl;
            }
        }

        if(work.getPhotoUrl() != null)
            fileService.deleteFiles(work.getPhotoUrl());
        if(work.getScriptUrl() != null)
            fileService.deleteFiles(work.getScriptUrl());
        if(work.getRecordUrl() != null)
            fileService.deleteFiles(work.getRecordUrl());

        work.updateWork(title, videoLink, fileUrlList[0], fileUrlList[1], fileUrlList[2], info, isRep);
    }

    @Transactional
    public void deleteWork(Long memberId, Long voiceId, Long workId) {
        validateMember(voiceId, memberId);
        Work work = workFindService.findById(workId);
        if(work.getPhotoUrl() != null)
            fileService.deleteFiles(work.getPhotoUrl());
        if(work.getScriptUrl() != null)
            fileService.deleteFiles(work.getScriptUrl());
        if(work.getRecordUrl() != null)
            fileService.deleteFiles(work.getRecordUrl());

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
