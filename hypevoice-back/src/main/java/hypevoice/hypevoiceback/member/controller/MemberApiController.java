package hypevoice.hypevoiceback.member.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.member.dto.MemberUpdateRequest;
import hypevoice.hypevoiceback.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<Long> update(@ExtractPayload Long memberId, @RequestBody MemberUpdateRequest memberRequest) {
        memberService.update(memberId, memberRequest.nickname(), memberRequest.profileUrl());
        return new ResponseEntity<>(memberId, HttpStatus.OK);
    }
}
