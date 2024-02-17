package hypevoice.hypevoiceback.comment.controller;

import hypevoice.hypevoiceback.comment.dto.CommentRequest;
import hypevoice.hypevoiceback.comment.service.CommentService;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/{boardId}")
    public ResponseEntity<Void> create(@ExtractPayload Long writerId, @PathVariable("boardId") Long boardId,
                                       @RequestPart(value = "request") @Valid CommentRequest request,
                                       @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        commentService.create(writerId, boardId, request.content(), multipartFile);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long writerId, @PathVariable("commentId") Long commentId) {
        commentService.delete(writerId, commentId);
        return ResponseEntity.ok().build();
    }
}
