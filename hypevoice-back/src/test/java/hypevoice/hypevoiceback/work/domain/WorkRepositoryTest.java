package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfoRepository;
import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.work.dto.WorkList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.CATEGORY_INFO_01;
import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Work [Repository Test] -> WorkRepository 테스트")
public class WorkRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VoiceRepository voiceRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    private Member member;
    private Voice voice;
    private Work work;
    private CategoryInfo categoryInfo;
    private WorkList[] workLists = new WorkList[3];

    @BeforeEach
    void setUp() {
        member = memberRepository.save(JAESIK.toMember());
        voice = voiceRepository.save(VOICE_01.toVoice(member));
        work = workRepository.save(WORK_01.toWork(voice));
        categoryInfo = categoryInfoRepository.save(CATEGORY_INFO_01.toCategoryInfo(work));

        Work work2 = workRepository.save(WORK_02.toWork(voice));
        Work work3 = workRepository.save(WORK_03.toWork(voice));

        workLists[0] = new WorkList(work.getId(), work.getTitle(), work.getVideoLink(), work.getPhotoUrl(), work.getScriptUrl(), work.getRecordUrl(), work.getInfo(), work.getIsRep());
        workLists[1] = new WorkList(work2.getId(), work2.getTitle(), work2.getVideoLink(), work2.getPhotoUrl(), work2.getScriptUrl(), work2.getRecordUrl(), work2.getInfo(), work2.getIsRep());
        workLists[2] = new WorkList(work3.getId(), work3.getTitle(), work3.getVideoLink(), work3.getPhotoUrl(), work3.getScriptUrl(), work3.getRecordUrl(), work3.getInfo(), work3.getIsRep());
    }

    @Test
    @DisplayName("보이스ID로 모든 작업물을 조회한다")
    void findAllByVoiceId() {

        List<WorkList> findWorkLists = workRepository.findAllByVoiceId(voice.getId());

        for(int i = 0 ; i < findWorkLists.size(); i++){
            assertThat(findWorkLists.get(i)).isEqualTo(workLists[findWorkLists.size() - 1 - i]);
        }

    }

    @Test
    @DisplayName("ID(PK)로 가장 최근에 수정된 대표 작업물을 조회한다")
    void findRepWorkByVoiceId() {

        Work findWork = workRepository.findRepWorkByVoiceId(voice.getId());

        assertThat(findWork.getId()).isEqualTo(workLists[2].workId());

    }
}
