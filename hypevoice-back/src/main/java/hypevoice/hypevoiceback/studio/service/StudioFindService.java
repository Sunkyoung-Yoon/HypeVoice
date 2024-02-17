package hypevoice.hypevoiceback.studio.service;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.domain.StudioRepository;
import hypevoice.hypevoiceback.studio.exception.StudioErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudioFindService {

    private final StudioRepository studioRepository;

    public Studio findById(Long studioId) {
        return studioRepository.findById(studioId).orElseThrow(() -> BaseException.type(StudioErrorCode.STUDIO_NOT_FOUND));
    }
}
