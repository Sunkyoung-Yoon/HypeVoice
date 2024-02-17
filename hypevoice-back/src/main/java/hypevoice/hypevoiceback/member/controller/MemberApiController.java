package hypevoice.hypevoiceback.member.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.member.dto.MemberResponse;
import hypevoice.hypevoiceback.member.dto.MemberUpdateRequest;
import hypevoice.hypevoiceback.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<Long> update(@ExtractPayload Long memberId, @RequestPart(value = "request") MemberUpdateRequest memberRequest,
                                       @RequestPart(value = "file", required = false) MultipartFile multipartFiles) {
        memberService.update(memberId, memberRequest.nickname(), multipartFiles);
        return new ResponseEntity<>(memberId, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<MemberResponse> read(@ExtractPayload Long memberId) {
        MemberResponse memberResponse = memberService.read(memberId);
        return new ResponseEntity<>(memberResponse, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@ExtractPayload Long memberId) {
        memberService.delete(memberId);
        return ResponseEntity.ok().build();
    }
}
