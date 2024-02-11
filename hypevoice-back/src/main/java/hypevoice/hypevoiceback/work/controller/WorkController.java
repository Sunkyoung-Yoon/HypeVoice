package hypevoice.hypevoiceback.work.controller;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoListRequest;
import hypevoice.hypevoiceback.categoryInfo.service.CategoryInfoService;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.work.dto.WorkRequest;
import hypevoice.hypevoiceback.work.dto.WorkResponse;
import hypevoice.hypevoiceback.work.service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voices/{voiceId}/works")
public class WorkController {

    private final WorkService workService;
    private final CategoryInfoService categoryInfoService;

    @PostMapping
    public ResponseEntity<Void> createWork(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId,
                                           @RequestPart(value = "request") WorkRequest request,
                                           @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles) {
        Long workId = workService.registerWork(memberId, voiceId, request.title(), request.videoLink(), request.info(), request.isRep(), multipartFiles);
        categoryInfoService.createCategoryInfo(memberId, workId, request.categoryInfoRequest().mediaClassification(), request.categoryInfoRequest().voiceTone(), request.categoryInfoRequest().voiceStyle(), request.categoryInfoRequest().gender(), request.categoryInfoRequest().age());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{workId}")
    public ResponseEntity<Void> updateWork(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId,
                                           @PathVariable("workId") Long workId, @RequestPart(value = "request") WorkRequest request,
                                           @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles) {
        workService.updateWork(memberId, voiceId, workId, request.title(), request.videoLink(), request.info(), request.isRep(), multipartFiles);
        categoryInfoService.updateCategoryInfo(memberId, workId, request.categoryInfoRequest().mediaClassification(), request.categoryInfoRequest().voiceTone(), request.categoryInfoRequest().voiceStyle(), request.categoryInfoRequest().gender(), request.categoryInfoRequest().age());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{workId}")
    public ResponseEntity<Void> deleteWork(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId,
                                           @PathVariable("workId") Long workId) {
        workService.deleteWork(memberId, voiceId, workId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{workId}")
    public ResponseEntity<WorkResponse> readWork(@PathVariable("voiceId") Long voiceId, @PathVariable("workId") Long workId) {
        WorkResponse workResponse = workService.readWork(voiceId, workId);
        return new ResponseEntity<>(workResponse, HttpStatus.OK);
    }

    @GetMapping("/{workId}/script")
    public ResponseEntity<String> readScriptUrl(@PathVariable("voiceId") Long voiceId, @PathVariable("workId") Long workId) {
        String scriptUrl = workService.readScriptUrl(voiceId, workId);
        return new ResponseEntity<>(scriptUrl, HttpStatus.OK);
    }

    @GetMapping("/{workId}/video")
    public ResponseEntity<String> readVideoLink(@PathVariable("voiceId") Long voiceId, @PathVariable("workId") Long workId) {
        String videoLink = workService.readVideoLink(voiceId, workId);
        return new ResponseEntity<>(videoLink, HttpStatus.OK);
    }

    @PutMapping("/{workId}")
    public ResponseEntity<Void> updateRepresentationWork(@ExtractPayload Long memberId, @PathVariable("voiceId") Long voiceId, @PathVariable("workId") Long workId) {
        workService.updateRepresentationWork(memberId, voiceId, workId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<WorkResponse>> readAllWork(@PathVariable("voiceId") Long voiceId) {
        List<WorkResponse> workResponseList = workService.readAllWork(voiceId);
        return new ResponseEntity<>(workResponseList, HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<WorkResponse>> readWorkByCategory(@PathVariable("voiceId") Long voiceId, @RequestBody CategoryInfoListRequest request) {
        List<WorkResponse> findWorkListByCategory = workService.readCategoryWork(voiceId, request.mediaClassificationValueList(), request.voiceToneValueList(), request.voiceStyleValueList(), request.genderValueList(), request.ageValueList());
        return new ResponseEntity<>(findWorkListByCategory, HttpStatus.OK);
    }
}
