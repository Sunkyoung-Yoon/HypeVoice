package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.board.dto.BoardResponse;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.board.domain.Category;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final MemberFindService memberFindService;
    private final BoardRepository boardRepository;
    private final BoardFindService boardFindService;

    @Transactional
    public Long create(Long writerId, String title, String content, String category){
        Member writer = memberFindService.findById(writerId);
        Category findCategory = Category.from(category);
        Board board = Board.createBoard(writer, title, content, findCategory);

        return boardRepository.save(board).getId();
    }

    @Transactional
    public void update(Long writerId, Long boardId, String title, String content){
        validateWriter(boardId, writerId);
        Board board = boardFindService.findById(boardId);

        board.updateBoard(title, content);
    }

    @Transactional
    public void delete(Long writerId, Long boardId){
        validateWriter(boardId, writerId);
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public BoardResponse read(Long boardId) {
        Board board = boardFindService.findById(boardId);
        board.updateView();
        Board readBoard = boardFindService.findById(boardId);
        return BoardResponse.builder()
                .boardId(readBoard.getId())
                .title(readBoard.getTitle())
                .content(readBoard.getContent())
                .view(readBoard.getView())
                .category(readBoard.getCategory().getValue())
                .createdDate(readBoard.getCreatedDate())
                .writerId(readBoard.getWriter().getId())
                .writerNickname(readBoard.getWriter().getNickname())
                .build();
    }

    private void validateWriter(Long boardId, Long writerId) {
        Board board = boardFindService.findById(boardId);
        if (!board.getWriter().getId().equals(writerId)) {
            throw BaseException.type(BoardErrorCode.USER_IS_NOT_BOARD_WRITER);
        }
    }
}
