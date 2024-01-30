package hypevoice.hypevoiceback.comment.service;

import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.comment.domain.CommentRepository;
import hypevoice.hypevoiceback.comment.exception.CommentErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentFindService {
    private final CommentRepository commentRepository;

    @Transactional
    public Comment findById(Long commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> BaseException.type(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
