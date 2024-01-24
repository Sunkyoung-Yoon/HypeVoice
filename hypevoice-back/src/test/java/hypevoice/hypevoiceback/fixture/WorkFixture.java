package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkFixture {
    Work1("한줄소개1", "vLink1", "pLink1", "sURL1", "rURL1", "디테일소개1"),
    Work2("한줄소개2", "vLink2", "pLink2", "sURL2", "rURL2", "디테일소개2"),
    Work3("한줄소개3", "vLink3", "pLink3", "sURL3", "rURL3", "디테일소개3")
    ;

    private final String intro;
    private final String videoLink;
    private final String photoURL;
    private final String scriptURL;
    private final String recordURL;
    private final String info;

    public Work toWork() {
        return Work.createWork(intro, videoLink, photoURL, scriptURL, recordURL, info);
    }

}
