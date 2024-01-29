package hypevoice.hypevoiceback.home;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    // 벡엔드 도커 배포 테스트에 사용할 컨트롤러
    @RequestMapping("/")
    public String sample() {
        return "Home";
    }
}
