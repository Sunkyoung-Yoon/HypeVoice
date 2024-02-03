package hypevoice.hypevoiceback.categoryInfo.controller;

import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoRequest;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoResponse;
import hypevoice.hypevoiceback.categoryInfo.service.CategoryInfoService;
import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/works/{workId}/categoryInfos")
public class CategoryInfoController {
    private final CategoryInfoService categoryInfoService;

    @PostMapping
    public ResponseEntity<Void> create(@ExtractPayload Long memberId, @PathVariable("workId") Long workId, @RequestBody @Valid CategoryInfoRequest request) {
        categoryInfoService.createCategoryInfo(memberId, workId, request.mediaClassification(), request.voiceTone(), request.voiceStyle(), request.gender(), request.age());

        return ResponseEntity.ok().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@ExtractPayload Long memberId, @PathVariable("workId") Long workId, @RequestBody @Valid CategoryInfoRequest request) {
        categoryInfoService.updateCategoryInfo(memberId, workId, request.mediaClassification(), request.voiceTone(), request.voiceStyle(), request.gender(), request.age());

        return ResponseEntity.ok().build();
    }

    // 카테고리들 모두 선택 후 확인 또는 완료버튼 클릭시
    @GetMapping
    public ResponseEntity<CategoryInfoResponse> read(@PathVariable("workId") Long workId) {
        CategoryInfoResponse categoryInfoResponse = categoryInfoService.readCategoryInfo(workId);
        return new ResponseEntity<>(categoryInfoResponse, HttpStatus.OK);
    }
}
