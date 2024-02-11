package hypevoice.hypevoiceback.voice.controller;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoListRequest;
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

    @GetMapping("/list/date")
    public ResponseEntity<List<VoiceCardListResponse>> readAllSortedByDate() {
        return new ResponseEntity<>(voiceService.readAllVoice(), HttpStatus.OK);
    }

    @GetMapping("/list/likes")
    public ResponseEntity<List<VoiceCardListResponse>> readAllSortedByLikes() {
        return new ResponseEntity<>(voiceService.readAllSortedByLikes(), HttpStatus.OK);
    }


    @GetMapping("/search")
    public ResponseEntity<List<VoiceCardListResponse>> searchVoice(@RequestParam("keyword") String keyword) {
        return new ResponseEntity<>(voiceService.searchVoice(keyword), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<VoiceCardListResponse>> filterVoiceByCategory(@RequestBody CategoryInfoListRequest request) {
        List<VoiceCardListResponse> findVoiceByCategory = voiceService.filterVoiceByCategory(request.mediaClassificationValueList(), request.voiceToneValueList(), request.voiceStyleValueList(), request.genderValueList(), request.ageValueList());
        return new ResponseEntity<>(findVoiceByCategory, HttpStatus.OK);
    }
}
