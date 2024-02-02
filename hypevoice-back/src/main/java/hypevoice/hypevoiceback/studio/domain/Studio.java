package hypevoice.hypevoiceback.studio.domain;

import hypevoice.hypevoiceback.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "studio")
public class Studio extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studio_id")
    private Long id;

    private String sessionId;
    private String title;
    private String intro;
    private int memberCount;
    private int limitNumber;
    private int isPublic;
    private String password;
    private int onAir;

    @Builder
    private Studio(String sessionId, String title, String intro, int limitNumber, int isPublic, String password) {
        this.sessionId = sessionId;
        this.title = title;
        this.intro = intro;
        this.memberCount = 1;
        this.limitNumber = limitNumber;
        this.isPublic = isPublic;
        this.password = password;
        this.onAir = 0;
    }

    public static Studio createStudio(String sessionId, String title, String intro, int limitNumber, int isPublic, String password) {
        return new Studio(sessionId, title, intro, limitNumber, isPublic, password);
    }

    public void updateStudio(String title, String intro, int limitNumber, int isPublic, String password) {
        this.title = title;
        this.intro = intro;
        this.limitNumber = limitNumber;
        this.isPublic = isPublic;
        this.password = password;
    }
}
