package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoList;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoValue;
import hypevoice.hypevoiceback.categoryInfo.service.CategoryInfoFindService;
import hypevoice.hypevoiceback.categoryInfo.service.CategoryInfoService;
import hypevoice.hypevoiceback.file.service.FileService;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.voice.service.VoiceFindService;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import hypevoice.hypevoiceback.work.dto.WorkList;
import hypevoice.hypevoiceback.work.dto.WorkResponse;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkService {
    private final WorkRepository workRepository;
    private final WorkFindService workFindService;
    private final VoiceFindService voiceFindService;
    private final FileService fileService;
    private final CategoryInfoService categoryInfoService;
    private final CategoryInfoFindService categoryInfoFindService;

    @Transactional
    public Long registerWork(Long memberId, Long voiceId, String title, String videoLink, String info, int isRep, MultipartFile[] multipartFiles) {
        validateMember(voiceId, memberId);

        Voice voice = voiceFindService.findById(voiceId);
        String[] fileUrlList = new String[3];
        if (multipartFiles != null) {
            for (int i = 0; i < multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                String fileUrl = null;
                if (file != null) {
                    fileUrl = fileService.uploadWorkFiles(file);
                    voice.increaseTotalSize(file.getSize());
                }

                fileUrlList[i] = fileUrl;
            }
        }

        Work work = Work.createWork(voice, title, videoLink, fileUrlList[0], fileUrlList[1], fileUrlList[2], info, isRep);
        return workRepository.save(work).getId();
    }

    @Transactional
    public void updateWork(Long memberId, Long voiceId, Long workId, String title, String videoLink, String info, int isRep, MultipartFile[] multipartFiles) {
        validateMember(voiceId, memberId);
        validateVoice(voiceId, workId);

        Work work = workFindService.findById(workId);

        String[] fileUrlList = new String[3];
        if (multipartFiles != null) {
            for (int i = 0; i < multipartFiles.length; i++) {
                MultipartFile file = multipartFiles[i];
                String fileUrl = null;
                if (file != null)
                    fileUrl = fileService.uploadWorkFiles(file);

                fileUrlList[i] = fileUrl;
            }
        }

        if (work.getPhotoUrl() != null)
            fileService.deleteFiles(work.getPhotoUrl());
        if (work.getScriptUrl() != null)
            fileService.deleteFiles(work.getScriptUrl());
        if (work.getRecordUrl() != null)
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
        CategoryInfo categoryInfo = categoryInfoFindService.findByWorkId(workId);

        return new WorkResponse(
                voiceId, workId, work.getTitle(), work.getVideoLink(), work.getPhotoUrl(), work.getScriptUrl(), work.getRecordUrl(), work.getInfo(), work.getIsRep(),
                new CategoryInfoValue(
                        workId,
                        categoryInfo.getMediaClassification().getTitle(),
                        categoryInfo.getVoiceStyle().getTitle(),
                        categoryInfo.getVoiceTone().getTitle(),
                        categoryInfo.getGender().getTitle(),
                        categoryInfo.getAge().getTitle()
                )
        );
    }

    @Transactional
    public List<WorkResponse> readAllWork(Long voiceId) {
        Voice voice = voiceFindService.findById(voiceId);

        List<WorkList> workLists = workFindService.findAllByVoiceId(voiceId);
        List<WorkResponse> workResponseList = new ArrayList<>();
        for (WorkList wl : workLists) {
            CategoryInfoList categoryInfoList = categoryInfoFindService.findCategoryInfoListByWorkId(wl.workId());
            workResponseList.add(
                    new WorkResponse(
                            voiceId, wl.workId(), wl.title(), wl.videoLink(), wl.photoUrl(), wl.scriptUrl(), wl.recordUrl(), wl.info(), wl.isRep(),
                            new CategoryInfoValue(
                                    wl.workId(),
                                    categoryInfoList.mediaClassification().getTitle(),
                                    categoryInfoList.voiceStyle().getTitle(),
                                    categoryInfoList.voiceTone().getTitle(),
                                    categoryInfoList.gender().getTitle(),
                                    categoryInfoList.age().getTitle()
                            )
                    )
            );
        }

        return workResponseList;
    }

    // 카테고리를 이용한 조회
    @Transactional
    public List<WorkResponse> readCategoryWork(Long voiceId, List<String> mediaValueList, List<String> voiceStyleValueList, List<String> voiceToneValueList, List<String> genderValueList, List<String> ageValueList) {
         validateExistVoice(voiceId);

        List<Long> workIdList = categoryInfoService.getWorkIdListByCategories(mediaValueList, voiceStyleValueList, voiceToneValueList, genderValueList, ageValueList);
        List<WorkResponse> findWorkResponse = new ArrayList<>();

        for (Long workId : workIdList) {
            Work work = workFindService.findById(workId);
            if(work != null && work.getVoice().getId().equals(voiceId))
            findWorkResponse.add(
                    new WorkResponse(
                            voiceId, workId, work.getTitle(), work.getVideoLink(), work.getPhotoUrl(), work.getScriptUrl(), work.getRecordUrl(), work.getInfo(), work.getIsRep(),
                            new CategoryInfoValue(
                                    workId,
                                    work.getCategoryInfo().getMediaClassification().getTitle(),
                                    work.getCategoryInfo().getVoiceStyle().getTitle(),
                                    work.getCategoryInfo().getVoiceTone().getTitle(),
                                    work.getCategoryInfo().getGender().getTitle(),
                                    work.getCategoryInfo().getAge().getTitle()
                            )
                    )
            );
        }

        return findWorkResponse;
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
