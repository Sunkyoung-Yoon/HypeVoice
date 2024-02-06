package hypevoice.hypevoiceback.categoryInfo.service;

import hypevoice.hypevoiceback.categoryInfo.domain.*;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoListResponse;
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
    public CategoryInfoListResponse findCategoryInfoListByWorkId(Long workId) {
        return categoryInfoRepository.findCategoryInfoListByWorkId(workId)
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }

    @Transactional
    public List<Long> findWorkIdByCategory(MediaClassification mediaClassification, VoiceTone voiceTone, VoiceStyle voiceStyle, Gender gender, Age age) {
        return categoryInfoRepository.findWorkIdByCategory(mediaClassification, voiceTone, voiceStyle, gender, age)
                .orElseThrow(() -> BaseException.type(CategoryInfoErrorCode.CATEGORY_NOT_FOUND));
    }
}
