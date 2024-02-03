package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.board.domain.BoardSearchType;
import hypevoice.hypevoiceback.board.domain.BoardSortCondition;
import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardListService {
    private final BoardRepository boardRepository;
    private final BoardFindService boardFindService;

    @Transactional
    public CustomBoardListResponse<BoardList> getBoardList(int page, String sortBy,
                                                           String searchBy, String searchWord) {
        BoardSortCondition boardSortCondition = BoardSortCondition.from(sortBy);
        BoardSearchType boardSearchType = BoardSearchType.from(searchBy);

        CustomBoardListResponse<BoardList> boardList = new CustomBoardListResponse<>();
        switch (boardSortCondition) {
            case TIME -> boardList = boardRepository.getBoardListOrderByTime(boardSearchType, searchWord, page);
            case HIT -> boardList = boardRepository.getBoardListOrderByHit(boardSearchType, searchWord, page);
        }

        List<BoardList> boardLists = getSortedBoardList(boardList);
        return new CustomBoardListResponse<>(boardList.getPageInfo(), boardLists);
    }

    private List<BoardList> getSortedBoardList(CustomBoardListResponse<BoardList> boardLists) {
        List<BoardList> boardList1 = boardLists.getBoardList();
        List<BoardList> boardListResponseList = new ArrayList<>();

        for (BoardList boardList : boardList1) {
            Board board = boardFindService.findById(boardList.getBoardId());
            BoardList boardListResponse = BoardList.builder()
                    .boardId(board.getId())
                    .title(board.getTitle())
                    .category(board.getCategory())
                    .view(board.getView())
                    .createdDate(board.getCreatedDate())
                    .writerId(board.getWriter().getId())
                    .writerNickname(board.getWriter().getNickname())
                    .build();
            boardListResponseList.add(boardListResponse);
        }
        return boardListResponseList;
    }
}
