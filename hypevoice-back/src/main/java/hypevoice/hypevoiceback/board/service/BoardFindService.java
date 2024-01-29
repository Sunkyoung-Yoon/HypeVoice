package hypevoice.hypevoiceback.board.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.board.exception.BoardErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardFindService {
    private final BoardRepository boardRepository;

    @Transactional
    public Board findById(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(() -> BaseException.type(BoardErrorCode.BOARD_NOT_FOUND));
    }
}

