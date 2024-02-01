package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkFixture {
    WORK_01("제목1", "vLink1", "pLink1", "sUrl1", "rUrl1", "디테일소개1", 1),
    WORK_02("제목2", "vLink2", "pLink2", "sUrl2", "rUrl2", "디테일소개2", 0),
    WORK_03("제목3", "vLink3", "pLink3", "sUrl3", "rUrl3", "디테일소개3", 1)
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
