package hypevoice.hypevoiceback.comment.service;

import hypevoice.hypevoiceback.comment.domain.Comment;
import hypevoice.hypevoiceback.comment.domain.CommentRepository;
import hypevoice.hypevoiceback.comment.dto.CommentList;
import hypevoice.hypevoiceback.comment.dto.CustomCommentListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentListService {
    private final CommentRepository commentRepository;
    private final CommentFindService commentFindService;

    @Transactional
    public CustomCommentListResponse<CommentList> getCommentList(Long boardId, int page) {
        CustomCommentListResponse<CommentList> commentList = commentRepository.getCommentListOrderByTime(boardId, page);

        List<CommentList> commentLists = getSortedCommentList(commentList);
        return new CustomCommentListResponse<>(commentList.pageInfo(), commentLists);
    }

    private List<CommentList> getSortedCommentList(CustomCommentListResponse<CommentList> commentLists) {
        List<CommentList> commentList1 = commentLists.commentList();
        List<CommentList> commentListResponseList = new ArrayList<>();

        for (CommentList commentList : commentList1) {
            Comment comment = commentFindService.findById(commentList.commentId());
            CommentList commentListResponse = CommentList.builder()
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .voiceCommentUrl(comment.getVoiceCommentUrl())
                    .createdDate(comment.getCreatedDate())
                    .writerId(comment.getWriter().getId())
                    .writerNickname(comment.getWriter().getNickname())
                    .build();
            commentListResponseList.add(commentListResponse);
        }
        return commentListResponseList;
    }
}
