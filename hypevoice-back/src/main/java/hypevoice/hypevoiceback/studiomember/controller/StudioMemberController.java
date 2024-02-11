package hypevoice.hypevoiceback.studiomember.controller;

import hypevoice.hypevoiceback.global.annotation.ExtractPayload;
import hypevoice.hypevoiceback.member.dto.MemberResponse;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import hypevoice.hypevoiceback.studiomember.service.StudioMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/studiomembers")
public class StudioMemberController {

    private final StudioMemberService studioMemberService;

    @GetMapping
    public ResponseEntity<StudioResponse> findStudioByMemberId(@ExtractPayload Long loginId) {
        return ResponseEntity.status(HttpStatus.OK).body(studioMemberService.findByMemberId(loginId));
    }

    @GetMapping("/{studioId}")
    public ResponseEntity<List<MemberResponse>> findMemberByStudioId(@ExtractPayload Long loginId, Long studioId) {
        return ResponseEntity.status(HttpStatus.OK).body(studioMemberService.findAllByStudioId(studioId));
    }

}
