package hypevoice.hypevoiceback.comment.query;

import hypevoice.hypevoiceback.comment.dto.CommentList;
import hypevoice.hypevoiceback.comment.dto.CustomCommentListResponse;

public interface CommentListQueryRepository {
    CustomCommentListResponse<CommentList> getCommentListOrderByTime(Long boardId, int page);
}
