package hypevoice.hypevoiceback;

import hypevoice.hypevoiceback.work.domain.Work;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class HypevoiceBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(HypevoiceBackApplication.class, args);
    }
}
