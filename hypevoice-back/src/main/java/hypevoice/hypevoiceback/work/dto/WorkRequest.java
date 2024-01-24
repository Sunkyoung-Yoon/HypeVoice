package hypevoice.hypevoiceback.work.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 등록, 수정 dto
public class WorkRequest {
    private String intro;
    private String videoLink;
    private String photoURL;
    private String scriptURL;
    private String recordURL;
    private String info;
    private String isRep;
}
