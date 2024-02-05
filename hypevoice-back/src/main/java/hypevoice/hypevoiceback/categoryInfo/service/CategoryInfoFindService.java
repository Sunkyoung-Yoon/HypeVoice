package hypevoice.hypevoiceback.categoryInfo.service;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfoRepository;
import hypevoice.hypevoiceback.categoryInfo.exception.CategoryInfoErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
