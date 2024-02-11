package hypevoice.hypevoiceback.comment.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record CustomCommentListResponse<C>(
        CustomPageable pageInfo, // pageable
        List<C> commentList // content

) {
    public CustomCommentListResponse(Page<C> page) {
        this(
                new CustomPageable(
                        page.getTotalPages(),
                        page.getTotalElements(),
                        page.hasNext(),
                        page.getNumberOfElements()
                ),
                page.getContent()
        );
    }

    public record CustomPageable(
            long totalPages,
            long totalElements,
            boolean hasNext,
            long numberOfElements
    ) {
    }
}