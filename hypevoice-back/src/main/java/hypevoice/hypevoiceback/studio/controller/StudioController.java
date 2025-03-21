package hypevoice.hypevoiceback.studio.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.studio.dto.*;
import hypevoice.hypevoiceback.studio.service.StudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studios")
public class StudioController {
    private final StudioService studioService;

    // 등록
    @PostMapping
    public ResponseEntity<StudioJoinResponse> createStudio(@ExtractPayload Long loginId, @RequestBody @Valid StudioRequest studioRequest) {
        System.out.println("로그 생성");
        StudioJoinResponse studioJoinResponse = studioService.createStudio(loginId, studioRequest);
        System.out.println("서비스 통과");

        return new ResponseEntity<>(studioJoinResponse, HttpStatus.OK);
    }

    // 수정
    @PatchMapping("/{studioId}")
    public ResponseEntity<Void> update(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @RequestBody StudioRequest studioRequest) {
        studioService.updateStudio(loginId, studioId, studioRequest);
        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/{studioId}")
    public ResponseEntity<Void> delete(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId) {
        studioService.deleteStudio(loginId, studioId);
        return ResponseEntity.ok().build();
    }

    // 상세 조회
    @GetMapping("/{studioId}")
    public ResponseEntity<StudioResponse> findOneStudio(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId) {
        return ResponseEntity.status(HttpStatus.OK).body(studioService.findOneStudio(loginId, studioId));
    }

    @PostMapping("/{studioId}/connect/public")
    public ResponseEntity<StudioJoinResponse> joinPublicStudio(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId) {
        return ResponseEntity.status(HttpStatus.OK).body(studioService.joinStudio(loginId, studioId, null));
    }

    @PostMapping("/{studioId}/connect/private")
    public ResponseEntity<StudioJoinResponse> joinPrivateStudio(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @RequestBody StudioJoinRequest studioJoinRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(studioService.joinStudio(loginId, studioId, studioJoinRequest.password()));
    }

    @GetMapping()
    public ResponseEntity<List<StudioResponse>> findAllStudios(@RequestParam(value = "word", required = false, defaultValue = "") String word, @RequestParam(value = "page") Integer page) {
        return ResponseEntity.status(HttpStatus.OK).body(studioService.findAll(word, page));
    }

    // 녹음 시작
    @PostMapping("/{studioId}/recording/start/individual")
    public ResponseEntity<String> startRecordingIndividual(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId) {
        System.out.println("레코딩 시작 ");
        String response = studioService.startRecording(studioId, loginId, true);
        System.out.println("레코딩 시작 서비스 완료");
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/{studioId}/recording/start")
    public ResponseEntity<String> startRecording(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @RequestParam(value = "isIndividual") Boolean isIndividual) {
        System.out.println("레코딩 시작 ");
        String response = studioService.startRecording(studioId, loginId, isIndividual);
        System.out.println("레코딩 시작 서비스 완료");
        System.out.println(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PostMapping("/{studioId}/recording/stop/{recordingId}")
    public ResponseEntity<String> stopRecording(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @PathVariable("recordingId") String recordingId) {
        String status = studioService.stopRecording(recordingId, loginId, studioId);
        return ResponseEntity.status(HttpStatus.OK).body(status);
    }

    @GetMapping("/{studioId}/recording/{recordingId}")
    public ResponseEntity<List<String>> getRecording(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @PathVariable("recordingId") String recordingId) {
        List<String> response = studioService.getRecordingUrl(recordingId, loginId, studioId);
        for(String s : response){
            System.out.println(s);
        }
        System.out.println("녹음 받아오기 서비스 완료");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{studioId}/recording/{recordingId}")
    public ResponseEntity<Void> deleteRecording(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @PathVariable("recordingId") String recordingId, @RequestBody StuidoRecordingRequest stuidoRecordingRequest) {
        studioService.deleteRecording(recordingId, loginId, studioId, stuidoRecordingRequest.urls());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{studioId}/connect/share")
    public ResponseEntity<String> getScreenShareToken(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId) {
        return ResponseEntity.status(HttpStatus.OK).body(studioService.screenShare(loginId,studioId));
    }

}
