package hypevoice.hypevoiceback.comment.controller;

import hypevoice.hypevoiceback.comment.dto.CommentRequest;
import hypevoice.hypevoiceback.comment.service.CommentService;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> create(@ExtractPayload Long writerId, @PathVariable("boardId") Long boardId,
                                       @RequestBody @Valid CommentRequest request) {
        commentService.create(writerId, boardId, request.content(), request.voiceCommentUrl());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable("commentId") Long commentId) {
        commentService.delete(writerId, commentId);
        return ResponseEntity.ok().build();
    }
}
