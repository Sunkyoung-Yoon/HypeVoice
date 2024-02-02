package hypevoice.hypevoiceback.studio.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studio.service.StudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studios")
public class StudioController {
    private final StudioService studioService;

    // 등록
    @PostMapping
    public ResponseEntity<Long> createSession(@ExtractPayload Long loginId, @RequestBody @Valid StudioRequest studioRequest) {
        Long studioId = studioService.createSession(studioRequest);
        return new ResponseEntity<>(studioId, HttpStatus.OK);
    }

    // 수정
    @PatchMapping("/{studioId}")
    public ResponseEntity<Void> update(@ExtractPayload Long loginId, @PathVariable("studioId") Long studioId, @RequestBody StudioRequest studioRequest) {
        studioService.updateStudio(studioId, studioRequest);
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
    public ResponseEntity<StudioResponse> findOneStudio(@PathVariable("studioId") Long studioId) {
        return ResponseEntity.status(HttpStatus.OK).body(studioService.findOneStudio(studioId));
    }
}
