package hypevoice.hypevoiceback.work.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Table(name = "work")
@Entity
public class Work {

    @Id
    @GeneratedValue
    @Column(name = "work_id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "voice_id")
//    private Voice voice;

    private String intro;

    @Column(name = "video_link")
    private String videoLink;

    @Column(name = "photo_url")
    private String photoURL;

    @Column(name = "script_url")
    private String scriptURL;

    @Column(name = "record_url")
    private String recordURL;

    private String info;

    @Column(name = "is_rep")
    private String isRep;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "modified_date")
    private String modifiedDate;

    @Builder
    public Work(String intro, String videoLink, String photoURL, String scriptURL,
                String recordURL, String info, String isRep) {
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoURL = photoURL;
        this.scriptURL = scriptURL;
        this.recordURL = recordURL;
        this.info = info;
        this.isRep = isRep;
    }

    public static Work createWork(String intro, String videoLink, String photoURL, String scriptURL,
                                  String recordURL, String info, String isRep) {
        return new Work(intro, videoLink, photoURL, scriptURL,
                recordURL, info, isRep);
    }

    public void updateWork(String intro, String videoLink, String photoURL, String scriptURL,
                String recordURL, String info, String isRep) {
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoURL = photoURL;
        this.scriptURL = scriptURL;
        this.recordURL = recordURL;
        this.info = info;
        this.isRep = isRep;
    }
}




