package hypevoice.hypevoiceback.voice.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.work.domain.Work;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "voice")
public class Voice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voice_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    private String name;
    private String imageUrl;
    private String intro;
    private String email;
    private String phone;
    private String addInfo;
    private int likes;

    @OneToMany(mappedBy = "voice", cascade = PERSIST, orphanRemoval = true)
    private List<Work> workList = new ArrayList<>();

    @Builder
    private Voice(Member member, String name, String imageUrl, String intro, String email, String phone, String addInfo) {
        this.member = member;
        this.name = name;
        this.imageUrl = imageUrl;
        this.intro = intro;
        this.email = email;
        this.phone = phone;
        this.addInfo = addInfo;
        this.likes = 0;
    }

    // 계정 생성시 voice를 자동으로 생성해야한다
    public static Voice createVoice(Member member, String name) {
        return new Voice(member, name, null, null, null, null, null);
    }

    public void updateVoice(String name, String imageUrl, String intro, String email, String phone, String addInfo) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.intro = intro;
        this.email = email;
        this.phone = phone;
        this.addInfo = addInfo;
    }

    public void increaseLikes() {
        this.likes = this.likes + 1;
    }

    public void decreaseLikes() {
        this.likes = this.likes - 1;
    }

}




