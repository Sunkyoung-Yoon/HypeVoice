package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.studio.domain.Studio;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudioFixture {
    STUDIO_FIXTURE1("session1","title1","intro1",1, 0,"1234"),
    STUDIO_FIXTURE2("session1","title2","intro2",5, 0,null),
    STUDIO_FIXTURE3("session1","title2","intro2",6, 1,"1234");

    private final String sessionId;
    private final String title;
    private final String intro;
    private final int limitNumber;
    private final int isPublic;
    private final String password;


    public Studio toStudio() {
        return Studio.createStudio(sessionId,title,intro,limitNumber,isPublic,password);
    }
}
