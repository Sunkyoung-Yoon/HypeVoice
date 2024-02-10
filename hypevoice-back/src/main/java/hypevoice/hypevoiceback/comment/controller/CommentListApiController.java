package hypevoice.hypevoiceback.comment.controller;

import hypevoice.hypevoiceback.comment.dto.CommentList;
import hypevoice.hypevoiceback.comment.dto.CustomCommentListResponse;
import hypevoice.hypevoiceback.comment.service.CommentListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentListApiController {
    private final CommentListService commentListService;

    @GetMapping("/{boardId}")
    public ResponseEntity<CustomCommentListResponse<CommentList>> commentList(@PathVariable Long boardId,
                                                                              @RequestParam(value = "page", defaultValue = "0", required = false) int page) {
        return ResponseEntity.ok(commentListService.getCommentList(boardId, page));
    }
}
