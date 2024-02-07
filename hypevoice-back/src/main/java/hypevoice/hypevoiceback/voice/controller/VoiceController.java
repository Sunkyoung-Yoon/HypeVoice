package hypevoice.hypevoiceback.voice.controller;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoRequest;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.voice.dto.VoiceCardListResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceUpdateRequest;
import hypevoice.hypevoiceback.voice.service.VoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    public ResponseEntity<VoiceReadResponse> read(@PathVariable("voiceId") Long voiceId) {
        return new ResponseEntity<>(voiceService.readDetailVoice(voiceId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<VoiceCardListResponse>> readAll() {
        return new ResponseEntity<>(voiceService.readAllVoice(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VoiceCardListResponse>> searchVoice(@RequestParam("keyword") String keyword) {
        return new ResponseEntity<>(voiceService.searchVoice(keyword), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<VoiceCardListResponse>> filterVoiceByCategory(@RequestBody CategoryInfoRequest request) {
        List<VoiceCardListResponse> findVoiceByCategory = voiceService.filterVoiceByCategory(request.mediaClassification(), request.voiceTone(), request.voiceStyle(), request.gender(), request.age());
        return new ResponseEntity<>(findVoiceByCategory, HttpStatus.OK);
    }
}
