package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.categoryInfo.domain.CategoryInfo;
import hypevoice.hypevoiceback.global.BaseTimeEntity;
import hypevoice.hypevoiceback.voice.domain.Voice;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.PERSIST;


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
    private String title;

    private String videoLink;
    private String photoUrl;
    private String scriptUrl;
    private String recordUrl;
    private String info;
    private int isRep;

    @OneToOne(mappedBy = "work", cascade = PERSIST, orphanRemoval = true)
    private CategoryInfo categoryInfo;

    @Builder
    private Work(Voice voice, String title, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info, int isRep) {
        this.voice = voice;
        this.title = title;
        this.videoLink = videoLink;
        this.photoUrl = photoUrl;
        this.scriptUrl = scriptUrl;
        this.recordUrl = recordUrl;
        this.info = info;
        this.isRep = isRep;
    }

    public static Work createWork(Voice voice, String title, String videoLink, String photoUrl, String scriptUrl, String recordUrl, String info, int isRep) {
        return new Work(voice, title, videoLink, photoUrl, scriptUrl, recordUrl, info, isRep);
    }

    public void updateWork(String title, String videoLink, String photoUrl, String scriptUrl,
                           String recordUrl, String info, int isRep) {
        this.title = title;
        this.videoLink = videoLink;
        this.photoUrl = photoUrl;
        this.scriptUrl = scriptUrl;
        this.recordUrl = recordUrl;
        this.info = info;
        this.isRep = isRep;
    }

    public void updateRep() {
        if (this.isRep == 1) {
            this.isRep = 0;
        } else {
            this.isRep = 1;
        }
    }
}
