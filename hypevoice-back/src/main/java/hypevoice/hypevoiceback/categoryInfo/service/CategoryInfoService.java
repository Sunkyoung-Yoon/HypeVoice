package hypevoice.hypevoiceback.categoryInfo.service;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.categoryInfo.exception.CategoryInfoErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.service.WorkFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryInfoService {

    private final CategoryInfoRepository categoryInfoRepository;
    private final CategoryInfoFindService categoryInfoFindService;
    private final WorkFindService workFindService;

    @Transactional
    public boolean createCategoryInfo(Long memberId, Long workId, String mediaClassification, String voiceStyle, String voiceTone, String gender, String age) {
        validateMember(workId, memberId);

        try {
            validateCategoryInfo(mediaClassification, voiceStyle, voiceTone, gender, age);
        } catch (BaseException e) {
            return false;
        }

        Work work = workFindService.findById(workId);
        MediaClassification findMediaClassification = MediaClassification.from(mediaClassification);
        VoiceStyle findVoiceStyle = VoiceStyle.from(voiceStyle);
        VoiceTone findVoiceTone = VoiceTone.from(voiceTone);
        Gender findGender = Gender.from(gender);
        Age findAge = Age.from(age);

        CategoryInfo categoryInfo = CategoryInfo.createCategoryInfo(work, findMediaClassification, findVoiceStyle, findVoiceTone, findGender, findAge);

        categoryInfoRepository.save(categoryInfo);
        return true;
    }

    @Transactional
    public void updateCategoryInfo(Long memberId, Long workId, String mediaClassification, String voiceStyle, String voiceTone, String gender, String age) {
        validateMember(workId, memberId);
        validateCategoryInfo(mediaClassification, voiceStyle, voiceTone, gender, age);

        Work work = workFindService.findById(workId);
        CategoryInfo categoryInfo = categoryInfoFindService.findByWorkId(work.getId());
        MediaClassification findMediaClassification = MediaClassification.from(mediaClassification);
        VoiceStyle findVoiceStyle = VoiceStyle.from(voiceStyle);
        VoiceTone findVoiceTone = VoiceTone.from(voiceTone);
        Gender findGender = Gender.from(gender);
        Age findAge = Age.from(age);

        categoryInfo.updateCategoryInfo(findMediaClassification, findVoiceStyle, findVoiceTone, findGender, findAge);
    }

    @Transactional
    public List<Long> getWorkIdListByCategories(List<String> mediaValueList, List<String> voiceStyleValueList, List<String> voiceToneValueList, List<String> genderValueList, List<String> ageValueList) {
        List<MediaClassification> mediaList = new ArrayList<>();
        List<VoiceStyle> voiceStyleList = new ArrayList<>();
        List<VoiceTone> voiceToneList = new ArrayList<>();
        List<Gender> genderList = new ArrayList<>();
        List<Age> ageList = new ArrayList<>();

        for (String s : mediaValueList) {
            if (mediaValueList.isEmpty()) break;
            mediaList.add(MediaClassification.from(s));
        }
        for (String s : voiceStyleValueList) {
            if (voiceStyleValueList.isEmpty()) break;
            voiceStyleList.add(VoiceStyle.from(s));
        }
        for (String s : voiceToneValueList) {
            if (voiceToneValueList.isEmpty()) break;
            voiceToneList.add(VoiceTone.from(s));
        }
        for (String s : genderValueList) {
            if (genderValueList.isEmpty()) break;
            genderList.add(Gender.from(s));
        }
        for (String s : ageValueList) {
            if (ageValueList.isEmpty()) break;
            ageList.add(Age.from(s));
        }

        List<Long> workIdList = categoryInfoFindService.findWorkIdByCategory(mediaList, voiceStyleList, voiceToneList, genderList, ageList);

        return workIdList;
    }

    private void validateMember(Long workId, Long memberId) {
        Work work = workFindService.findById(workId);
        if (!work.getVoice().getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER);
        }
    }

    private void validateCategoryInfo(String mediaClassification, String voiceStyle, String voiceTone, String gender, String age) {
        if (mediaClassification.isEmpty() || voiceStyle.isEmpty() || voiceTone.isEmpty() || gender.isEmpty() || age.isEmpty()) {
            throw BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND);
        }
    }

}
