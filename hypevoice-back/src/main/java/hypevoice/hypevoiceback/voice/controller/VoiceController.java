package hypevoice.hypevoiceback.voice.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceUpdateRequest;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voices")
public class VoiceController {
    private final VoiceService voiceService;

    @PatchMapping("/{voiceId}")
    public ResponseEntity<Void> update(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId,
                                       @RequestPart(value = "request") VoiceUpdateRequest voiceUpdateRequest,
                                       @RequestPart(value = "file", required = false) MultipartFile multipartFile) {
        voiceService.updateVoice(
                memberId,
                voiceId,
                voiceUpdateRequest.name(),
                voiceUpdateRequest.intro(),
                voiceUpdateRequest.email(),
                voiceUpdateRequest.phone(),
                voiceUpdateRequest.addInfo(),
                multipartFile);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{voiceId}")
    public ResponseEntity<VoiceReadResponse> read(@PathVariable("voiceId") Long voiceId){
        VoiceReadResponse voiceReadResponse = voiceService.readDetailVoice(voiceId);
        return ResponseEntity.ok(voiceReadResponse);
    }
}
