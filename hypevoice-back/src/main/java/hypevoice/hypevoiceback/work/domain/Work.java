package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
import hypevoice.hypevoiceback.voice.domain.Voice;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voice_id", referencedColumnName = "voice_id")
    private Voice voice;

    @Column(nullable = false)
    private String intro;

    private String videoLink;
    private String photoUrl;
    private String scriptUrl;
    private String recordUrl;
    private String info;
    private int isRep;

    @Builder
    private Work(Voice voice, String intro, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info) {
        this.voice = voice;
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoUrl = photoUrl;
        this.scriptUrl = scriptUrl;
        this.recordUrl = recordUrl;
        this.info = info;
        this.isRep = 0;
    }

    public static Work createWork(Voice voice, String intro, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info) {
        return new Work(voice, intro, videoLink, photoUrl, scriptUrl, recordUrl, info);
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




