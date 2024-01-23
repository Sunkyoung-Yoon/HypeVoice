package hypevoice.hypevoiceback.common;


import hypevoice.hypevoiceback.global.config.JpaAuditingConfig;
import hypevoice.hypevoiceback.global.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({QueryDslConfig.class, JpaAuditingConfig.class})
public class RepositoryTest {
}