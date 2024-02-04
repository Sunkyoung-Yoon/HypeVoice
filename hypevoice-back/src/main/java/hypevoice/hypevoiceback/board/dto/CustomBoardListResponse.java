package hypevoice.hypevoiceback.board.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record CustomBoardListResponse<T>(
        CustomPageable pageInfo, // pageable
        List<T> boardList // content

) {
    public CustomBoardListResponse(Page<T> page) {
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

    public CustomBoardListResponse() {
        this(
                null,
                null
        );
    }

    public record CustomPageable (
            long totalPages,
            long totalElements,
            boolean hasNext,
            long numberOfElements
    ) {
    }
}
