package hypevoice.hypevoiceback.work.controller;

import hypevoice.hypevoiceback.work.dto.WorkRequest;
import hypevoice.hypevoiceback.work.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voices/{voiceId}/works")
public class WorkController {
    private final WorkService workService;

    @PostMapping
    public ResponseEntity<Void> createWork(@PathVariable Long voiceId, @RequestBody WorkRequest request) {
        workService.createWork(voiceId, request.info(), request.videoLink(), request.photoUrl(), request.scriptUrl(), request.recordUrl(), request.info());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{workId}")
    public ResponseEntity<Void> updateWork(@PathVariable Long voiceId, @PathVariable Long workId, @RequestBody WorkRequest request){
        workService.updateWork(voiceId,workId,request.info(), request.videoLink(), request.photoUrl(), request.scriptUrl(), request.recordUrl(), request.info());
        return ResponseEntity.ok().build();
    }

}
