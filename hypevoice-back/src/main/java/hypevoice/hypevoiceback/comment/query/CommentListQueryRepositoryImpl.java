package hypevoice.hypevoiceback.comment.query;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hypevoice.hypevoiceback.comment.dto.CommentList;
import hypevoice.hypevoiceback.comment.dto.CustomCommentListResponse;
import hypevoice.hypevoiceback.comment.dto.QCommentList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static hypevoice.hypevoiceback.comment.domain.QComment.comment;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentListQueryRepositoryImpl implements CommentListQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public CustomCommentListResponse<CommentList> getCommentListOrderByTime(Long boardId, int page) {
        Pageable pageable = PageRequest.of(page, 20);
        List<CommentList> commentLists = query
                .selectDistinct(new QCommentList(comment.id, comment.content, comment.voiceCommentUrl, comment.createdDate, comment.writer.id, comment.writer.nickname))
                .from(comment)
                .where(comment.board.id.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(count(comment.id))
                .from(comment)
                .where(comment.board.id.eq(boardId));

        return new CustomCommentListResponse<>(PageableExecutionUtils.getPage(commentLists, pageable, countQuery::fetchOne));

    }
}
