package hypevoice.hypevoiceback.voice.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceUpdateRequest;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voices")
public class VoiceController {
    private final VoiceService voiceService;

    @PatchMapping("/{voiceId}")
    public ResponseEntity<Void> update(@ExtractPayload Long memberId,
            @PathVariable Long voiceId, @RequestBody VoiceUpdateRequest voiceUpdateRequest) {
        voiceService.updateVoice(
                memberId,
                voiceId,
                voiceUpdateRequest.name(),
                voiceUpdateRequest.imageUrl(),
                voiceUpdateRequest.intro(),
                voiceUpdateRequest.email(),
                voiceUpdateRequest.phone(),
                voiceUpdateRequest.addInfo());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{voiceId}")
    public ResponseEntity<VoiceReadResponse> read(@PathVariable("voiceId") Long voiceId){
        VoiceReadResponse voiceReadResponse = voiceService.readDetailVoice(voiceId);
        return ResponseEntity.ok(voiceReadResponse);
    }
}
