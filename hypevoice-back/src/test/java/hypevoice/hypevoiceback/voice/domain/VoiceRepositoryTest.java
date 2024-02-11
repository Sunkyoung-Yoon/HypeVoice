package hypevoice.hypevoiceback.voice.domain;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfoRepository;
import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.voice.dto.VoiceCard;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.domain.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.*;
import static hypevoice.hypevoiceback.fixture.MemberFixture.*;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.*;
import static hypevoice.hypevoiceback.fixture.WorkFixture.*;
import static org.assertj.core.api.Assertions.assertThat;


public class VoiceRepositoryTest extends RepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private VoiceRepository voiceRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    private Member member1;
    private Member member2;
    private Member member3;
    private Voice voice1;
    private Voice voice2;
    private Voice voice3;
    private Work work1;
    private Work work2;
    private Work work3;
    private CategoryInfo categoryInfo1;
    private CategoryInfo categoryInfo2;
    private CategoryInfo categoryInfo3;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(JAESIK.toMember());
        member2 = memberRepository.save(GABIN.toMember());
        member3 = memberRepository.save(SUNKYOUNG.toMember());
        voice1 = voiceRepository.save(VOICE_01.toVoice(member1));
        voice2 = voiceRepository.save(VOICE_02.toVoice(member2));
        voice3 = voiceRepository.save(VOICE_03.toVoice(member3));
        work1 = workRepository.save(WORK_01.toWork(voice1));
        work2 = workRepository.save(WORK_02.toWork(voice2));
        work3 = workRepository.save(WORK_03.toWork(voice3));
        categoryInfo1 = categoryInfoRepository.save(CATEGORY_INFO_01.toCategoryInfo(work1));
        categoryInfo2 = categoryInfoRepository.save(CATEGORY_INFO_02.toCategoryInfo(work2));
        categoryInfo3 = categoryInfoRepository.save(CATEGORY_INFO_03.toCategoryInfo(work3));
    }

    @Test
    @DisplayName("보이스 이름 또는 작업물 제목을 키워드로 보이스를 검색한다")
    void findByKeyword() {
        List<VoiceCard> findVoiceCards = voiceRepository.findByKeyword("키워드").orElseThrow();

        assertThat(findVoiceCards.size()).isEqualTo(2);

    }

}
