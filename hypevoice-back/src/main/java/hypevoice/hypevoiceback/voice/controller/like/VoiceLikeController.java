package hypevoice.hypevoiceback.voice.controller.like;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.voice.service.like.VoiceLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voices/{voiceId}/likes")
public class VoiceLikeController {
    private final VoiceLikeService voiceLikeService;

    @PostMapping
    public ResponseEntity<Void> register(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId) {
        voiceLikeService.register(memberId, voiceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> cancel(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId) {
        voiceLikeService.cancel(memberId, voiceId);
        return ResponseEntity.ok().build();
    }
}
