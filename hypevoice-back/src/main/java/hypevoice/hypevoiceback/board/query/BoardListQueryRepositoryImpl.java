package hypevoice.hypevoiceback.board.query;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hypevoice.hypevoiceback.board.domain.BoardSearchType;
import hypevoice.hypevoiceback.board.dto.BoardList;
import hypevoice.hypevoiceback.board.dto.CustomBoardListResponse;
import hypevoice.hypevoiceback.board.dto.QBoardList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static hypevoice.hypevoiceback.board.domain.QBoard.board;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardListQueryRepositoryImpl implements BoardListQueryRepository{
    private final JPAQueryFactory query;

    @Override
    public CustomBoardListResponse<BoardList> getBoardListOrderByTime(BoardSearchType boardSearchType, String searchWord, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<BoardList> boardLists = query
                .selectDistinct(new QBoardList(board.id, board.title, board.view, board.category, board.createdDate, board.writer.id, board.writer.nickname))
                .from(board)
                .where(search(boardSearchType, searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdDate.desc())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(count(board.id))
                .from(board)
                .where(search(boardSearchType, searchWord));

        return new CustomBoardListResponse<>(PageableExecutionUtils.getPage(boardLists, pageable, countQuery::fetchOne));
    }

    @Override
    public CustomBoardListResponse<BoardList> getBoardListOrderByHit(BoardSearchType boardSearchType, String searchWord, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<BoardList> boardLists = query
                .selectDistinct(new QBoardList(board.id, board.title, board.view, board.category, board.createdDate, board.writer.id, board.writer.nickname))
                .from(board)
                .where(search(boardSearchType, searchWord))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.view.desc())
                .fetch();

        JPAQuery<Long> countQuery = query
                .select(count(board.id))
                .from(board)
                .where(search(boardSearchType, searchWord));

        return new CustomBoardListResponse<>(PageableExecutionUtils.getPage(boardLists, pageable, countQuery::fetchOne));
    }

    private BooleanExpression search(BoardSearchType boardSearchType, String searchWord) {
        if (searchWord == null || searchWord.isEmpty()) {
            return null;
        } else {
            switch (boardSearchType) {
                case WRITER -> {
                    return board.writer.nickname.contains(searchWord);
                }
                case TITLE -> {
                    return board.title.contains(searchWord);
                }
                case CONTENT -> {
                    return board.content.contains(searchWord);
                }
                case TITLE_AND_CONTENT -> {
                    return board.title.contains(searchWord).or(board.content.contains(searchWord));
                }
                default -> {
                    return null;
                }
            }
        }
    }
}
