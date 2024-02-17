package hypevoice.hypevoiceback.work.dto;

// 작업물 리스트에 들어갈 필드들
public record WorkList(
        Long workId,
        String title,
        String videoLink,
        String photoUrl,
        String scriptUrl,
        String recordUrl,
        String info,
        int isRep
){

}
