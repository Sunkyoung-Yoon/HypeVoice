package hypevoice.hypevoiceback.studiomember.domain;

import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.studio.domain.Studio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "studio_member")
public class StudioMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studio_member_id")
    private Long id;

    private int isHost;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "studio_id", referencedColumnName = "studio_id")
    private Studio studio;

    private StudioMember(Member member, Studio studio, int isHost) {
        this.member = member;
        this.studio = studio;
        this.isHost = isHost;
    }

    public static StudioMember createStudioMember(Member member, Studio studio, int isHost) {
        return new StudioMember(member, studio, isHost);
    }

}
