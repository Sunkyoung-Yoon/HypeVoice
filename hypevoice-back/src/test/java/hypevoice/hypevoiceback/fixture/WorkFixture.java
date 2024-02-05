package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkFixture {
    WORK_01("제목1", "vLink1", null, null, null, "디테일소개1", 1),
    WORK_02("제목2", "vLink2", null, null, null, "디테일소개2", 0),
    WORK_03("제목3", "vLink3", null, null, null, "디테일소개3", 1)
    ;

    private final String title;
    private final String videoLink;
    private final String photoUrl;
    private final String scriptUrl;
    private final String recordUrl;
    private final String info;
    private final int isRep;

    public Work toWork(Voice voice) {
        return Work.createWork(voice, title, videoLink, photoUrl, scriptUrl, recordUrl, info, isRep);
    }

}
