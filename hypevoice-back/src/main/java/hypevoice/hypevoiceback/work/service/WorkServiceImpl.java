package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;

    @Override
    public void registWork(Work work) {
        workRepository.save(work);
    }

}
