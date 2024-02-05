package hypevoice.hypevoiceback.board.controller;

import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;
import hypevoice.hypevoiceback.board.service.BoardListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardListApiController {
    private final BoardListService boardListService;

    @GetMapping
    public ResponseEntity<CustomBoardListResponse<BoardList>> boardList(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                                        @RequestParam(value = "sort", required = false, defaultValue = "최신순") String sortBy,
                                                                        @RequestParam(value = "search", required = false, defaultValue = "제목") String searchBy,
                                                                        @RequestParam(value = "word", required = false, defaultValue = "") String searchWord) {
        return ResponseEntity.ok(boardListService.getBoardList(page, sortBy, searchBy, searchWord));
    }
}
