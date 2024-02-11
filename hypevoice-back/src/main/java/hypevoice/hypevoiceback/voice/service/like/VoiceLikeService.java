package hypevoice.hypevoiceback.voice.service.like;

import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.like.VoiceLike;
import hypevoice.hypevoiceback.voice.domain.like.VoiceLikeRepository;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.voice.service.VoiceFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VoiceLikeService {
    private final MemberFindService memberFindService;
    private final VoiceFindService voiceFindService;
    private final VoiceLikeRepository voiceLikeRepository;

    @Transactional
    public void register(Long memberId, Long voiceId) {
        validateSelfVoiceLike(memberId, voiceId);
        validateSAlreadyVoiceLike(memberId, voiceId);

        Member member = memberFindService.findById(memberId);
        Voice voice = voiceFindService.findById(voiceId);
        VoiceLike voiceLike = VoiceLike.registerVoiceLike(member, voice);

        voiceLikeRepository.save(voiceLike);
    }

    @Transactional
    public void cancel(Long memberId, Long voiceId){
        validateCancel(memberId, voiceId);
        voiceLikeRepository.deleteByMemberIdAndVoiceId(memberId, voiceId);
    }

    @Transactional
    public void deleteByMember(Member member) {
        voiceLikeRepository.deleteByMember(member);
    }

    @Transactional
    public boolean check(Long memberId, Long voiceId) {
        return voiceLikeRepository.existsByMemberIdAndVoiceId(memberId, voiceId);
    }

    private void validateSelfVoiceLike(Long memberId, Long voiceId) {
        Voice voice = voiceFindService.findById(voiceId);
        if (voice.getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.SELF_VOICE_LIKE_NOT_ALLOWED);
        }
    }

    private void validateSAlreadyVoiceLike(Long memberId, Long voiceId) {
        if (voiceLikeRepository.existsByMemberIdAndVoiceId(memberId, voiceId)) {
            throw BaseException.type(VoiceErrorCode.ALREADY_VOICE_LIKE);
        }
    }

    private void validateCancel(Long memberId, Long voiceId) {
        if (!voiceLikeRepository.existsByMemberIdAndVoiceId(memberId, voiceId)) {
            throw BaseException.type(VoiceErrorCode.VOICE_LIKE_NOT_FOUND);
        }
    }
}
