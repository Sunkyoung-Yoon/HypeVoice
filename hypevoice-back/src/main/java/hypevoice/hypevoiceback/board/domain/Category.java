package hypevoice.hypevoiceback.board.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    FEEDBACK("FEEDBACK", "피드백")
    ;

    private final String feedback;
    private final String title;
}
