package hypevoice.hypevoiceback.comment.service;

import hypevoice.hypevoiceback.board.domain.Board;
import hypevoice.hypevoiceback.board.service.BoardFindService;
import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.comment.domain.CommentRepository;
import hypevoice.hypevoiceback.comment.exception.CommentErrorCode;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final MemberFindService memberFindService;
    private final BoardFindService boardFindService;
    private final CommentRepository commentRepository;
    private final CommentFindService commentFindService;

    @Transactional
    public void create(Long writerId, Long boardId, String content, String voiceCommentUrl){
        Member writer = memberFindService.findById(writerId);
        Board board = boardFindService.findById(boardId);
        Comment comment = Comment.createComment(writer, board, content, voiceCommentUrl);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long writerId, Long commentId){
        validateWriter(commentId, writerId);
        commentRepository.deleteById(commentId);
    }

    private void validateWriter(Long commentId, Long writerId) {
        Comment comment = commentFindService.findById(commentId);
        if (!comment.getWriter().getId().equals(writerId)) {
            throw BaseException.type(CommentErrorCode.USER_IS_NOT_COMMENT_WRITER);
        }
    }
}
