package hypevoice.hypevoiceback.work.service;

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

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.JAESIK;
import static hypevoice.hypevoiceback.fixture.VoiceFixture.VOICE_01;
import static hypevoice.hypevoiceback.fixture.WorkFixture.WORK_01;
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
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @BeforeEach
    void setup() {
        member = memberRepository.save(JAESIK.toMember());
        notMember = memberRepository.save(GABIN.toMember());
        voice = voiceRepository.save(VOICE_01.toVoice(member));
        work = workRepository.save(WORK_01.toWork(voice));
    }

    @Test
    @DisplayName("작업물 등록에 성공한다")
    void register() {
        // then
        Work findWork = workFindService.findById(work.getId());
        assertAll(
                () -> assertThat(findWork.getTitle()).isEqualTo("제목1"),
                () -> assertThat(findWork.getVideoLink()).isEqualTo("vLink1"),
                () -> assertThat(findWork.getPhotoUrl()).isEqualTo("pLink1"),
                () -> assertThat(findWork.getScriptUrl()).isEqualTo("sUrl1"),
                () -> assertThat(findWork.getRecordUrl()).isEqualTo("rUrl1"),
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
            assertThatThrownBy(() -> workService.updateWork(notMember.getId(), voice.getId(), work.getId(), "제목2", "vLink2", "pLink2", "sUrl2", "rUrl2", "디테일소개2", 1))
                    .isInstanceOf(BaseException.class)
                    .hasMessage(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER.getMessage());
        }

        @Test
        @DisplayName("작업물 수정에 성공한다")
        void success() {
            // given
            workService.updateWork(member.getId(), voice.getId(), work.getId(), "제목2", "vLink2", "pLink2", "sUrl2", "rUrl2", "디테일소개2", 0);

            // when
            Work findWork = workFindService.findById(work.getId());

            // then
            assertAll(
                    () -> assertThat(findWork.getTitle()).isEqualTo("제목2"),
                    () -> assertThat(findWork.getVideoLink()).isEqualTo("vLink2"),
                    () -> assertThat(findWork.getPhotoUrl()).isEqualTo("pLink2"),
                    () -> assertThat(findWork.getScriptUrl()).isEqualTo("sUrl2"),
                    () -> assertThat(findWork.getRecordUrl()).isEqualTo("rUrl2"),
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
}
