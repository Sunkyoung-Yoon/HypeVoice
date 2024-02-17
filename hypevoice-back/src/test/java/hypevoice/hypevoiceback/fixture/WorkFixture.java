package hypevoice.hypevoiceback.fixture;

import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.work.domain.Work;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkFixture {
    WORK_01("키워드1", "vLink1", null, null, null, "디테일소개1", 1),
    WORK_02("키워드2", "vLink2", null, null, null, "디테일소개2", 1),
    WORK_03("제목2", "vLink3", null, null, null, "디테일소개3", 1),
    WORK_04("제목4", "vLink4", null, null, null, "디테일소개4", 0),
    WORK_05("제목5", "vLink5", null, null, null, "디테일소개5", 1),
    WORK_06("키워드6", "vLink6", null, null, null, "디테일소개6", 0),
    WORK_07("제목7", "vLink7", null, null, null, "디테일소개7", 0),
    WORK_08("제목8", "vLink8", null, null, null, "디테일소개8", 1),
    WORK_09("제목9", "vLink9", null, null, null, "디테일소개9", 1),
    WORK_10("제목10", "vLink10", null, null, null, "디테일소개10", 1);

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
