package hypevoice.hypevoiceback.categoryInfo.service;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoValue;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.service.WorkFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryInfoService {

    private final CategoryInfoRepository categoryInfoRepository;
    private final CategoryInfoFindService categoryInfoFindService;
    private final WorkFindService workFindService;

    @Transactional
    public void createCategoryInfo(Long memberId, Long workId, String mediaClassification, String voiceTone, String voiceStyle, String gender, String age) {
        validateMember(workId, memberId);

        Work work = workFindService.findById(workId);
        MediaClassification findMediaClassification = MediaClassification.from(mediaClassification);
        VoiceTone findVoiceTone = VoiceTone.from(voiceTone);
        VoiceStyle findVoiceStyle = VoiceStyle.from(voiceStyle);
        Gender findGender = Gender.from(gender);
        Age findAge = Age.from(age);

        CategoryInfo categoryInfo = CategoryInfo.createCategoryInfo(work, findMediaClassification, findVoiceTone, findVoiceStyle, findGender, findAge);

        categoryInfoRepository.save(categoryInfo);
    }

    @Transactional
    public void updateCategoryInfo(Long memberId, Long workId, String mediaClassification, String voiceTone, String voiceStyle, String gender, String age) {
        validateMember(workId, memberId);

        Work work = workFindService.findById(workId);
        CategoryInfo categoryInfo = categoryInfoFindService.findByWorkId(work.getId());
        MediaClassification findMediaClassification = MediaClassification.from(mediaClassification);
        VoiceTone findVoiceTone = VoiceTone.from(voiceTone);
        VoiceStyle findVoiceStyle = VoiceStyle.from(voiceStyle);
        Gender findGender = Gender.from(gender);
        Age findAge = Age.from(age);

        categoryInfo.updateCategoryInfo(findMediaClassification, findVoiceTone, findVoiceStyle, findGender, findAge);
    }

    @Transactional
    public CategoryInfoValue readCategoryInfo(Long workId) {
        CategoryInfo categoryInfo = categoryInfoFindService.findByWorkId(workId);

        return CategoryInfoValue.builder()
                .workId(workId)
                .mediaClassificationValue(categoryInfo.getMediaClassification().getValue())
                .voiceToneValue(categoryInfo.getVoiceTone().getValue())
                .voiceStyleValue(categoryInfo.getVoiceStyle().getValue())
                .genderValue(categoryInfo.getGender().getValue())
                .ageValue(categoryInfo.getAge().getValue())
                .build();
    }

    @Transactional
    public List<Long> getWorkIdList(String mediaClassification, String voiceTone, String voiceStyle, String gender, String age) {
        MediaClassification findMediaClassification = MediaClassification.from(mediaClassification);
        VoiceTone findVoiceTone = VoiceTone.from(voiceTone);
        VoiceStyle findVoiceStyle = VoiceStyle.from(voiceStyle);
        Gender findGender = Gender.from(gender);
        Age findAge = Age.from(age);
        List<Long> workIdList = categoryInfoFindService.findWorkIdByCategory(findMediaClassification, findVoiceTone, findVoiceStyle, findGender, findAge);

        return workIdList;
    }

    private void validateMember(Long workId, Long memberId) {
        Work work = workFindService.findById(workId);
        if (!work.getVoice().getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER);
        }
    }
}
