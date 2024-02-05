package hypevoice.hypevoiceback.studiomember.domain;

import hypevoice.hypevoiceback.common.RepositoryTest;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.studio.domain.Studio;
import hypevoice.hypevoiceback.studio.domain.StudioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static hypevoice.hypevoiceback.fixture.MemberFixture.GABIN;
import static hypevoice.hypevoiceback.fixture.MemberFixture.SUNKYOUNG;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("StudioMember [Repository Test] -> StudioMemberRepository 테스트")
public class StudioMemberRepositoryTest extends RepositoryTest {

    @Autowired
    private StudioMemberRepository studioMemberRepository;
    @Autowired
    private StudioRepository studioRepository;
    @Autowired
    private MemberRepository memberRepository;
    private StudioMember studioMember;
    private Studio[] studio = new Studio[2];
    private Member[] member = new Member[2];

    @BeforeEach
    void setUp() {
        studio[0] = studioRepository.save(STUDIO_FIXTURE1.toStudio());
        studio[1] = studioRepository.save(STUDIO_FIXTURE2.toStudio());
        member[0] = memberRepository.save(SUNKYOUNG.toMember());
        member[1] = memberRepository.save(GABIN.toMember());

    }

    @Test
    @DisplayName("스튜디오에 입장한 회원 ID와 스튜디오 ID 를 이용하여 스튜디오멤버 정보가 존재하는지 확인한다")
    void existsByMemberIdAndStudioId() {
        // given
        studioMemberRepository.save(StudioMember.createStudioMember(member[0], studio[0], 0));

        // when
        boolean actual1 = studioMemberRepository.existsByMemberIdAndStudioId(member[0].getId(), studio[0].getId());
        boolean actual2 = studioMemberRepository.existsByMemberIdAndStudioId(member[0].getId(), studio[1].getId());

        // then
        assertAll(
                () -> assertThat(actual1).isTrue(),
                () -> assertThat(actual2).isFalse()
        );
    }

    @Test
    @DisplayName("스튜디오에 입장한 회원 ID와 스튜디오 ID 를 이용하여 스튜디오멤버 정보를 삭제한다")
    void deleteByMemberIdAndStudioId() {
        // given
        studioMemberRepository.save(StudioMember.createStudioMember(member[0], studio[0], 0));

        // when
        studioMemberRepository.deleteByMemberIdAndStudioId(member[0].getId(), studio[0].getId());

        // then
        assertThat(studioMemberRepository.existsByMemberIdAndStudioId(member[0].getId(), studio[0].getId())).isFalse();

    }

}
