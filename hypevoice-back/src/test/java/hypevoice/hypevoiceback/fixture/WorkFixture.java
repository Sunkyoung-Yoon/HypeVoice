package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkFixture {
    Work1("한줄소개1", "vLink1", "pLink1", "sUrl1", "rUrl1", "디테일소개1"),
    Work2("한줄소개2", "vLink2", "pLink2", "sUrl2", "rUrl2", "디테일소개2"),
    Work3("한줄소개3", "vLink3", "pLink3", "sUrl3", "rUrl3", "디테일소개3")
    ;

    private final String intro;
    private final String videoLink;
    private final String photoUrl;
    private final String scriptUrl;
    private final String recordUrl;
    private final String info;

    public Work toWork(Voice voice) {
        return Work.createWork(voice, intro, videoLink, photoUrl, scriptUrl, recordUrl, info);
    }

}
