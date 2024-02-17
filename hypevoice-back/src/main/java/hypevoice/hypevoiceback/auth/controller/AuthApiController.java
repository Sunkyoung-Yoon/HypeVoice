package hypevoice.hypevoiceback.auth.controller;

import hypevoice.hypevoiceback.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthApiController {
    private final AuthService authService;

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@ExtractPayload Long memberId) {
        authService.logout(memberId);
        return ResponseEntity.ok().build();
    }
}
