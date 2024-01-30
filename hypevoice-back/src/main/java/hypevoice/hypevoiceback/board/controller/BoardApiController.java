package hypevoice.hypevoiceback.board.controller;

import hypevoice.hypevoiceback.board.dto.BoardRequest;
import hypevoice.hypevoiceback.board.dto.BoardResponse;
import hypevoice.hypevoiceback.board.service.BoardService;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardApiController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<Long> create(@ExtractPayload Long writerId,
                                       @RequestBody @Valid BoardRequest request) {
        Long boardId = boardService.create(writerId, request.title(), request.content(), request.category());
        return new ResponseEntity<>(boardId, HttpStatus.OK);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<Void> update(@ExtractPayload Long writerId, @PathVariable("boardId") Long boardId,
                                       @RequestBody @Valid BoardRequest request) {
        boardService.update(writerId, boardId, request.title(), request.content());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable("boardId") Long boardId) {
        boardService.delete(writerId, boardId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> readBoard(@PathVariable("boardId") Long boardId) {
        BoardResponse boardResponse = boardService.read(boardId);
        return new ResponseEntity<>(boardResponse, HttpStatus.OK);
    }
}
