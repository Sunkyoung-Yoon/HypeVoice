package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
//import hypevoice.hypevoiceback.voice.domain.Voice;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
@Table(name = "work")

public class Work extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "voice_id")
//    private Voice voice;

    @Column(nullable = false)
    private String intro;

    private String videoLink;
    private String photoUrl;
    private String scriptUrl;
    private String recordUrl;
    private String info;
    private int isRep;

    @Builder
    private Work(String intro, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info) {
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoUrl = photoUrl;
        this.scriptUrl = scriptUrl;
        this.recordUrl = recordUrl;
        this.info = info;
        this.isRep = 0;
    }

    public static Work createWork(String intro) {
        return new Work(intro, null, null, null, null, null);
    }

    public void updateWork(String intro, String videoLink, String photoUrl, String scriptUrl,
                           String recordUrl, String info) {
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoUrl = photoUrl;
        this.scriptUrl = scriptUrl;
        this.recordUrl = recordUrl;
        this.info = info;
    }

    public void updateRep() {
        if (this.isRep == 1) {
            this.isRep = 0;
        } else {
            this.isRep = 1;
        }
    }
}




