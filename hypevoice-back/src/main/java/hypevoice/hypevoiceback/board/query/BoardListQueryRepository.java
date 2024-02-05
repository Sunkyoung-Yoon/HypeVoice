package hypevoice.hypevoiceback.board.query;

import hypevoice.hypevoiceback.board.domain.BoardSearchType;
import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;

public interface BoardListQueryRepository {
    CustomBoardListResponse<BoardList> getBoardListOrderByTime(BoardSearchType boardSearchType, String searchWord, int page);
    CustomBoardListResponse<BoardList> getBoardListOrderByHit(BoardSearchType boardSearchType, String searchWord, int page);
}
