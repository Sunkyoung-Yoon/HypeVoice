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
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.*;
import static hypevoice.hypevoiceback.fixture.MemberFixture.*;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.*;
import static hypevoice.hypevoiceback.fixture.WorkFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


public class VoiceRepositoryTest extends RepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VoiceRepository voiceRepository;

    @Autowired
    private WorkRepository workRepository;

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;

    private Member[] members = new Member[10];
    private Voice[] voices = new Voice[10];
    private Work[] works = new Work[10];
    private CategoryInfo[] categoryInfos = new CategoryInfo[10];

    @BeforeEach
    void setUp() {

        members[0] = memberRepository.save(SUNKYOUNG.toMember());
        members[1] = memberRepository.save(GABIN.toMember());
        members[2] = memberRepository.save(JAESIK.toMember());
        members[3] = memberRepository.save(MEMBER_01.toMember());
        members[4] = memberRepository.save(MEMBER_02.toMember());
        members[5] = memberRepository.save(MEMBER_03.toMember());
        members[6] = memberRepository.save(MEMBER_04.toMember());
        members[7] = memberRepository.save(MEMBER_05.toMember());
        members[8] = memberRepository.save(MEMBER_06.toMember());
        members[9] = memberRepository.save(MEMBER_07.toMember());

        voices[0] = voiceRepository.save(VOICE_01.toVoice(members[0]));
        voices[1] = voiceRepository.save(VOICE_02.toVoice(members[1]));
        voices[2] = voiceRepository.save(VOICE_03.toVoice(members[2]));
        voices[3] = voiceRepository.save(VOICE_04.toVoice(members[3]));
        voices[4] = voiceRepository.save(VOICE_05.toVoice(members[4]));
        voices[5] = voiceRepository.save(VOICE_06.toVoice(members[5]));
        voices[6] = voiceRepository.save(VOICE_07.toVoice(members[6]));
        voices[7] = voiceRepository.save(VOICE_08.toVoice(members[7]));
        voices[8] = voiceRepository.save(VOICE_09.toVoice(members[8]));
        voices[9] = voiceRepository.save(VOICE_10.toVoice(members[9]));

        works[0] = workRepository.save(WORK_01.toWork(voices[0]));
        works[1] = workRepository.save(WORK_02.toWork(voices[1]));
        works[2] = workRepository.save(WORK_03.toWork(voices[2]));
        works[3] = workRepository.save(WORK_04.toWork(voices[3]));
        works[4] = workRepository.save(WORK_05.toWork(voices[4]));
        works[5] = workRepository.save(WORK_06.toWork(voices[5]));
        works[6] = workRepository.save(WORK_07.toWork(voices[6]));
        works[7] = workRepository.save(WORK_08.toWork(voices[7]));
        works[8] = workRepository.save(WORK_09.toWork(voices[8]));
        works[9] = workRepository.save(WORK_10.toWork(voices[9]));

        categoryInfos[0] = categoryInfoRepository.save(CATEGORY_INFO_01.toCategoryInfo(works[0]));
        categoryInfos[1] = categoryInfoRepository.save(CATEGORY_INFO_02.toCategoryInfo(works[1]));
        categoryInfos[2] = categoryInfoRepository.save(CATEGORY_INFO_03.toCategoryInfo(works[2]));
        categoryInfos[3] = categoryInfoRepository.save(CATEGORY_INFO_04.toCategoryInfo(works[3]));
        categoryInfos[4] = categoryInfoRepository.save(CATEGORY_INFO_05.toCategoryInfo(works[4]));
        categoryInfos[5] = categoryInfoRepository.save(CATEGORY_INFO_06.toCategoryInfo(works[5]));
        categoryInfos[6] = categoryInfoRepository.save(CATEGORY_INFO_07.toCategoryInfo(works[6]));
        categoryInfos[7] = categoryInfoRepository.save(CATEGORY_INFO_08.toCategoryInfo(works[7]));
        categoryInfos[8] = categoryInfoRepository.save(CATEGORY_INFO_09.toCategoryInfo(works[8]));
        categoryInfos[9] = categoryInfoRepository.save(CATEGORY_INFO_10.toCategoryInfo(works[9]));

    }

    @Test
    @DisplayName("보이스 이름 또는 작업물 제목을 키워드로 보이스를 검색한다")
    void findByKeyword() {
        List<VoiceCard> findVoiceCards1 = voiceRepository.findByKeyword("name").orElseThrow();
        List<VoiceCard> findVoiceCards2 = voiceRepository.findByKeyword("키워드").orElseThrow();

        assertAll(
                () -> assertThat(findVoiceCards1.size()).isEqualTo(4),
                () -> assertThat(findVoiceCards2.size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("보이스의 좋아요 순으로 정렬한다")
    void findAllSortedByLikes() {
        for (int i = 0; i < 10; i++) voices[9].increaseLikes();
        for (int i = 0; i < 9; i++) voices[0].increaseLikes();
        List<Voice> findVoiceList = voiceRepository.findAllSortedByLikes(PageRequest.of(0, 8)).orElseThrow();

        assertAll(
                () -> assertThat(findVoiceList.get(0).getName()).isEqualTo("name7"),
                () -> assertThat(findVoiceList.get(1).getName()).isEqualTo("윤선경")
        );
    }

    @Test
    @DisplayName("보이스의 수정 날짜순으로 정렬한다")
    void findAllSortedByDate() {
        voices[0].updateVoice("수정1", null, null, null, null, null);

        List<Voice> findVoiceList = voiceRepository.findAllSortedByDate(PageRequest.of(0, 8)).orElseThrow();

        assertAll(
                () -> assertThat(findVoiceList.get(0).getName()).isEqualTo("수정1"),
                () -> assertThat(findVoiceList.get(1).getName()).isEqualTo("name7")
        );
    }

}
