package hypevoice.hypevoiceback.work.service;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import hypevoice.hypevoiceback.categoryInfo.domain.MediaClassification;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoValue;
import hypevoice.hypevoiceback.common.ServiceTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.dto.WorkResponse;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.*;
import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Work [Service Layer] -> WorkService 테스트")
public class WorkServiceTest extends ServiceTest {

    @Autowired
    private WorkService workService;

    @Autowired
    private WorkFindService workFindService;

    private Member member;
    private Member notMember;
    private Voice voice;
    private Work work;
    private final Work[] workArray = new Work[10];
    private final CategoryInfo[] categoryInfoArray = new CategoryInfo[10];
    private List<String> mediaClassificationValueList = new ArrayList<>();
    private List<String> voiceToneValueList = new ArrayList<>();
    private List<String> voiceStyleValueList = new ArrayList<>();
    private List<String> genderValueList = new ArrayList<>();
    private List<String> ageValueList = new ArrayList<>();
    private final int WORK_SIZE = 10;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        member = memberRepository.save(JAESIK.toMember());
        notMember = memberRepository.save(GABIN.toMember());
        voice = voiceRepository.save(VOICE_01.toVoice(member));

        work = workArray[0] = workRepository.save(WORK_01.toWork(voice));
        workArray[1] = workRepository.save(WORK_02.toWork(voice));
        workArray[2] = workRepository.save(WORK_03.toWork(voice));
        workArray[3] = workRepository.save(WORK_04.toWork(voice));
        workArray[4] = workRepository.save(WORK_05.toWork(voice));
        workArray[5] = workRepository.save(WORK_06.toWork(voice));
        workArray[6] = workRepository.save(WORK_07.toWork(voice));
        workArray[7] = workRepository.save(WORK_08.toWork(voice));
        workArray[8] = workRepository.save(WORK_09.toWork(voice));
        workArray[9] = workRepository.save(WORK_10.toWork(voice));

        categoryInfoArray[0] = categoryInfoRepository.save(CATEGORY_INFO_01.toCategoryInfo(workArray[0]));
        categoryInfoArray[1] = categoryInfoRepository.save(CATEGORY_INFO_02.toCategoryInfo(workArray[1]));
        categoryInfoArray[2] = categoryInfoRepository.save(CATEGORY_INFO_03.toCategoryInfo(workArray[2]));
        categoryInfoArray[3] = categoryInfoRepository.save(CATEGORY_INFO_04.toCategoryInfo(workArray[3]));
        categoryInfoArray[4] = categoryInfoRepository.save(CATEGORY_INFO_05.toCategoryInfo(workArray[4]));
        categoryInfoArray[5] = categoryInfoRepository.save(CATEGORY_INFO_06.toCategoryInfo(workArray[5]));
        categoryInfoArray[6] = categoryInfoRepository.save(CATEGORY_INFO_07.toCategoryInfo(workArray[6]));
        categoryInfoArray[7] = categoryInfoRepository.save(CATEGORY_INFO_08.toCategoryInfo(workArray[7]));
        categoryInfoArray[8] = categoryInfoRepository.save(CATEGORY_INFO_09.toCategoryInfo(workArray[8]));
        categoryInfoArray[9] = categoryInfoRepository.save(CATEGORY_INFO_10.toCategoryInfo(workArray[9]));

        mediaClassificationValueList = new ArrayList<>();
        voiceToneValueList = new ArrayList<>();
        voiceStyleValueList = new ArrayList<>();
        genderValueList = new ArrayList<>();
        ageValueList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mediaClassificationValueList.add(categoryInfoArray[i].getMediaClassification().getValue());
            voiceToneValueList.add(categoryInfoArray[i].getVoiceTone().getValue());
            voiceStyleValueList.add(categoryInfoArray[i].getVoiceStyle().getValue());
            genderValueList.add(categoryInfoArray[i].getGender().getValue());
            ageValueList.add(categoryInfoArray[i].getAge().getValue());
        }
    }

    @Test
    @DisplayName("작업물 등록에 성공한다")
    void register() {
        // then
        Work findWork = workFindService.findById(work.getId());
        assertAll(
                () -> assertThat(findWork.getTitle()).isEqualTo("키워드1"),
                () -> assertThat(findWork.getVideoLink()).isEqualTo("vLink1"),
                () -> assertThat(findWork.getPhotoUrl()).isEqualTo(null),
                () -> assertThat(findWork.getScriptUrl()).isEqualTo(null),
                () -> assertThat(findWork.getRecordUrl()).isEqualTo(null),
                () -> assertThat(findWork.getInfo()).isEqualTo("디테일소개1"),
                () -> assertThat(findWork.getIsRep()).isEqualTo(1),
                () -> assertThat(findWork.getCreatedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(findWork.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter)),
                () -> assertThat(findWork.getVoice()).isEqualTo(voice)
        );
    }

    @Nested
    @DisplayName("작업물 수정")
    class update {
        @Test
        @DisplayName("다른 사람의 작업물은 수정할 수 없다")
        void throwExceptionByMemberNotWorkMember() {
            // when - then
            assertThatThrownBy(() -> workService.updateWork(notMember.getId(), voice.getId(), work.getId(), "제목2", "vLink2", "디테일소개2", 1, null))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER.getMessage());
        }

        @Test
        @DisplayName("작업물 수정에 성공한다")
        void success() {
            // given
            workService.updateWork(member.getId(), voice.getId(), work.getId(), "제목2", "vLink2", "디테일소개2", 0, null);

            // when
            Work findWork = workFindService.findById(work.getId());

            // then
            assertAll(
                    () -> assertThat(findWork.getTitle()).isEqualTo("제목2"),
                    () -> assertThat(findWork.getVideoLink()).isEqualTo("vLink2"),
                    () -> assertThat(findWork.getPhotoUrl()).isEqualTo(null),
                    () -> assertThat(findWork.getScriptUrl()).isEqualTo(null),
                    () -> assertThat(findWork.getRecordUrl()).isEqualTo(null),
                    () -> assertThat(findWork.getInfo()).isEqualTo("디테일소개2"),
                    () -> assertThat(findWork.getIsRep()).isEqualTo(0),
                    () -> assertThat(findWork.getModifiedDate().format(formatter)).isEqualTo(LocalDateTime.now().format(formatter))
            );
        }
    }

    @Nested
    @DisplayName("작업물 삭제")
    class delete {
        @Test
        @DisplayName("다른 사람의 작업물은 삭제할 수 없다")
        void throwExceptionByMemberNotWorkMember() {
            // when - then
            assertThatThrownBy(() -> workService.deleteWork(notMember.getId(), voice.getId(), work.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER.getMessage());
        }

        @Test
        @DisplayName("작업물 삭제에 성공한다")
        void success() {
            // given
            workService.deleteWork(member.getId(), voice.getId(), work.getId());

            // when - then
            assertThatThrownBy(() -> workFindService.findById(work.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(WorkErrorCode.WORK_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("작업물 상세 조회")
    class readWork {
        @Test
        @DisplayName("작업물 상세 조회에 성공한다")
        void success() {
            // when
            WorkResponse workResponse = workService.readWork(voice.getId(), work.getId());

            // then
            assertAll(
                    () -> assertThat(workResponse.voiceId()).isEqualTo(voice.getId()),
                    () -> assertThat(workResponse.workId()).isEqualTo(work.getId()),
                    () -> assertThat(workResponse.title()).isEqualTo(work.getTitle()),
                    () -> assertThat(workResponse.videoLink()).isEqualTo(work.getVideoLink()),
                    () -> assertThat(workResponse.photoUrl()).isEqualTo(work.getPhotoUrl()),
                    () -> assertThat(workResponse.scriptUrl()).isEqualTo(work.getScriptUrl()),
                    () -> assertThat(workResponse.recordUrl()).isEqualTo(work.getRecordUrl()),
                    () -> assertThat(workResponse.info()).isEqualTo(work.getInfo()),
                    () -> assertThat(workResponse.isRep()).isEqualTo(work.getIsRep())
            );
        }
    }

    @Nested
    @DisplayName("작업물의 대본 상세 조회")
    class readWorkScript {
        @Test
        @DisplayName("작업물의 대본 상세 조회에 성공한다")
        void success() {
            // when
            String findScriptUrl = workService.readScriptUrl(voice.getId(), work.getId());

            // then
            assertThat(findScriptUrl).isEqualTo(work.getScriptUrl());
        }
    }

    @Nested
    @DisplayName("작업물의 영상 상세 조회")
    class readWorkVideo {
        @Test
        @DisplayName("작업물의 영상 상세 조회에 성공한다")
        void success() {
            // when
            String findVideoLink = workService.readVideoLink(voice.getId(), work.getId());

            // then
            assertThat(findVideoLink).isEqualTo(work.getVideoLink());
        }
    }

    @Nested
    @DisplayName("작업물 대표 등록 수정")
    class updateRepresentationWork {
        @Test
        @DisplayName("다른 사람의 작업물의 대표등록을 수정할 수 없다")
        void throwExceptionByMemberNotWorkMember() {
            // when - then
            assertThatThrownBy(() -> workService.updateRepresentationWork(notMember.getId(), voice.getId(), work.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER.getMessage());
        }

        @Test
        @DisplayName("작업물의 대표 등록 수정에 성공한다")
        void success() {
            // given
            workService.updateRepresentationWork(member.getId(), voice.getId(), work.getId());

            // when : 1 -> 0
            int findIsRep = work.getIsRep();

            // then
            assertThat(findIsRep).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("작업물 전체 조회")
    class readAllWork {
        @Test
        @DisplayName("작업물 전체 조회에 성공한다")
        void success() {
            // when
            List<WorkResponse> allWork = workService.readAllWork(voice.getId());

            // then
            assertThat(allWork.size()).isEqualTo(WORK_SIZE);
        }
    }

}
