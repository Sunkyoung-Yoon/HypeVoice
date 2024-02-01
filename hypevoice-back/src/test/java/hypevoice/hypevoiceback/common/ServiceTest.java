package hypevoice.hypevoiceback.common;

import hypevoice.hypevoiceback.auth.domain.TokenRepository;
import hypevoice.hypevoiceback.board.domain.BoardRepository;
import hypevoice.hypevoiceback.comment.domain.CommentRepository;
import hypevoice.hypevoiceback.member.domain.MemberRepository;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.voice.domain.like.VoiceLikeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class ServiceTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected TokenRepository tokenRepository;

    @Autowired
    protected BoardRepository boardRepository;

    @Autowired
    protected CommentRepository commentRepository;

    @Autowired
    protected VoiceRepository voiceRepository;

    @Autowired
    protected VoiceLikeRepository voiceLikeRepository;

    @AfterEach
    void clearDatabase() {
        databaseCleaner.cleanUpDatabase();
    }

    public void flushAndClear() {
        databaseCleaner.flushAndClear();
    }
}