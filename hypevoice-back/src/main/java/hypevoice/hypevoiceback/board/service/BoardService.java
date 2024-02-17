package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.board.dto.BoardResponse;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.file.service.FileService;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.board.domain.Category;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final MemberFindService memberFindService;
    private final BoardRepository boardRepository;
    private final BoardFindService boardFindService;
    private final FileService fileService;

    @Transactional
    public Long create(Long writerId, String title, String content, String category, MultipartFile file){
        Member writer = memberFindService.findById(writerId);
        Category findCategory = Category.from(category);

        String recordUrl = null;
        if (file != null)
            recordUrl = fileService.uploadBoardFiles(file);

        Board board = Board.createBoard(writer, title, content, recordUrl, findCategory);

        return boardRepository.save(board).getId();
    }

    @Transactional
    public void update(Long writerId, Long boardId, String title, String content, MultipartFile file){
        validateWriter(boardId, writerId);
        Board board = boardFindService.findById(boardId);

        String recordUrl = null;
        if (file != null)
            recordUrl = fileService.uploadBoardFiles(file);

        if(board.getRecordUrl() != null)
            fileService.deleteFiles(board.getRecordUrl());

        board.updateBoard(title, content, recordUrl);
    }

    @Transactional
    public void delete(Long writerId, Long boardId){
        validateWriter(boardId, writerId);
        Board board = boardFindService.findById(boardId);

        if(board.getRecordUrl() != null)
            fileService.deleteFiles(board.getRecordUrl());

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
                .recordUrl(readBoard.getRecordUrl())
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
