package hypevoice.hypevoiceback.work.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
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

    private String intro;
    private String videoLink;
    private String photoURL;
    private String scriptURL;
    private String recordURL;
    private String info;
    private int isRep;

    @Builder
    private Work(String intro, String videoLink, String photoURL, String scriptURL,
                 String recordURL, String info) {
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoURL = photoURL;
        this.scriptURL = scriptURL;
        this.recordURL = recordURL;
        this.info = info;
        this.isRep = 0;
    }

    public static Work createWork(String intro, String videoLink, String photoURL, String scriptURL, String recordURL, String info) {
        return new Work(intro, videoLink, photoURL, scriptURL, recordURL, info);
    }

    public void updateWork(String intro, String videoLink, String photoURL, String scriptURL,
                           String recordURL, String info) {
        this.intro = intro;
        this.videoLink = videoLink;
        this.photoURL = photoURL;
        this.scriptURL = scriptURL;
        this.recordURL = recordURL;
        this.info = info;
    }

    public void updateRep() {
        if(this.isRep == 1){
            this.isRep = 0;
        }
        else{
            this.isRep = 1;
        }
    }
}




