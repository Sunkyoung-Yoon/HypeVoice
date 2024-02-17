package hypevoice.hypevoiceback.categoryInfo.service;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoList;
import hypevoice.hypevoiceback.categoryInfo.exception.CategoryInfoErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryInfoFindService {
    private final CategoryInfoRepository categoryInfoRepository;

    @Transactional
    public CategoryInfo findByWorkId(Long workId) {
        return categoryInfoRepository.findByWorkId(workId)
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public CategoryInfoList findCategoryInfoListByWorkId(Long workId) {
        return categoryInfoRepository.findCategoryInfoListByWorkId(workId)
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public List<Long> findWorkIdByCategory(List<MediaClassification> mediaList, List<VoiceStyle> voiceStyleList, List<VoiceTone> voiceToneList, List<Gender> genderList, List<Age> ageList) {
        if (mediaList.isEmpty()) mediaList = null;
        if (voiceStyleList.isEmpty()) voiceStyleList = null;
        if (voiceToneList.isEmpty()) voiceToneList = null;
        if (genderList.isEmpty()) genderList = null;
        if (ageList.isEmpty()) ageList = null;

        return categoryInfoRepository.findWorkIdByCategory(mediaList, voiceStyleList, voiceToneList, genderList, ageList)
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }
}
