package hypevoice.hypevoiceback.voice.service;

import hypevoice.hypevoiceback.categoryInfo.service.CategoryInfoService;
import hypevoice.hypevoiceback.file.service.FileService;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.member.domain.Member;
import hypevoice.hypevoiceback.member.service.MemberFindService;
import hypevoice.hypevoiceback.voice.domain.Voice;
import hypevoice.hypevoiceback.voice.domain.VoiceRepository;
import hypevoice.hypevoiceback.voice.dto.VoiceCard;
import hypevoice.hypevoiceback.voice.dto.VoiceCardListResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.work.domain.Work;
import hypevoice.hypevoiceback.work.service.WorkFindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VoiceService {

    private final VoiceRepository voiceRepository;
    private final VoiceFindService voiceFindService;
    private final MemberFindService memberFindService;
    private final FileService fileService;
    private final WorkFindService workFindService;
    private final CategoryInfoService categoryInfoService;

    @Transactional
    public void createVoice(Long memberId, String name) {
        Member member = memberFindService.findById(memberId);
        Voice voice = Voice.createVoice(member, name);
        voiceRepository.save(voice);
    }

    @Transactional
    public void updateVoice(Long memberId, Long voiceId, String name, String intro, String email,
                            String phone, String addInfo, MultipartFile file) {
        validateMember(voiceId, memberId);
        Voice voiceUpdate = voiceFindService.findById(voiceId);

        String profileImageUrl = null;
        if (file != null)
            profileImageUrl = fileService.uploadVoiceFiles(file);

        if (voiceUpdate.getImageUrl() != null)
            fileService.deleteFiles(voiceUpdate.getImageUrl());

        voiceUpdate.updateVoice(name, profileImageUrl, intro, email, phone, addInfo);
    }

    @Transactional
    public VoiceReadResponse readDetailVoice(Long voiceId) {
        Voice voiceDetail = voiceFindService.findById(voiceId);

        return VoiceReadResponse.builder()
                .name(voiceDetail.getName())
                .imageUrl(voiceDetail.getImageUrl())
                .intro(voiceDetail.getIntro())
                .addInfo(voiceDetail.getAddInfo())
                .email(voiceDetail.getEmail())
                .phone(voiceDetail.getPhone())
                .likes(voiceDetail.getLikes())
                .build();
    }

    @Transactional
    public List<VoiceCardListResponse> readAllVoice() {
        List<Voice> voiceList = voiceRepository.findAll();
        List<VoiceCard> voiceCardList = new ArrayList<>();

        for (Voice v : voiceList) {
            Work work = workFindService.findRepWorkByVoiceId(v.getId());
            if (work != null) {
                voiceCardList.add(
                        VoiceCard.builder()
                                .voiceId(v.getId())
                                .photoUrl(work.getPhotoUrl())
                                .categoryInfo(work.getCategoryInfo())
                                .title(work.getTitle())
                                .recordUrl(work.getRecordUrl())
                                .imageUrl(v.getImageUrl())
                                .name(v.getName())
                                .likes(v.getLikes())
                                .build()
                );
            }
        }

        return getVoiceCardListResponses(voiceCardList);
    }

    @Transactional
    public List<VoiceCardListResponse> readAllSortedByLikes() {
        List<Voice> voiceList = voiceFindService.findAllSortedByLikes();
        List<VoiceCard> voiceCardList = new ArrayList<>();

        for (Voice v : voiceList) {
            Work work = workFindService.findRepWorkByVoiceId(v.getId());
            if (work != null) {
                voiceCardList.add(
                        VoiceCard.builder()
                                .voiceId(v.getId())
                                .photoUrl(work.getPhotoUrl())
                                .categoryInfo(work.getCategoryInfo())
                                .title(work.getTitle())
                                .recordUrl(work.getRecordUrl())
                                .imageUrl(v.getImageUrl())
                                .name(v.getName())
                                .likes(v.getLikes())
                                .build()
                );
            }
        }

        return getVoiceCardListResponses(voiceCardList);
    }

    @Transactional
    public List<VoiceCardListResponse> searchVoice(String keyword) {
        List<VoiceCard> voiceCardList = voiceFindService.findByKeyword(keyword);
        return getVoiceCardListResponses(voiceCardList);
    }

    // 카테고리를 이용한 조회
    @Transactional
    public List<VoiceCardListResponse> filterVoiceByCategory(List<String> mediaValueList, List<String> voiceToneValueList, List<String> voiceStyleValueList, List<String> genderValueList, List<String> ageValueList) {
        List<VoiceCardListResponse> voiceCardListResponseList = this.readAllVoice();
        List<Long> workIdList = categoryInfoService.getWorkIdListByCategories(mediaValueList, voiceToneValueList, voiceStyleValueList, genderValueList, ageValueList);
        List<VoiceCard> voiceCardList = new ArrayList<>();

        for (VoiceCardListResponse v : voiceCardListResponseList) {
            for (Long workId : workIdList) {
                Work w = workFindService.findRepWorkByVoiceId(v.voiceId());
                if (w != null && w.getId().equals(workId)) {
                    voiceCardList.add(
                            VoiceCard.builder()
                                    .voiceId(v.voiceId())
                                    .photoUrl(w.getPhotoUrl())
                                    .categoryInfo(w.getCategoryInfo())
                                    .title(w.getTitle())
                                    .recordUrl(w.getRecordUrl())
                                    .imageUrl(w.getPhotoUrl())
                                    .name(v.name())
                                    .likes(v.likes())
                                    .build()
                    );
                }
            }
        }

        return getVoiceCardListResponses(voiceCardList);
    }


    private List<VoiceCardListResponse> getVoiceCardListResponses(List<VoiceCard> voiceList) {
        List<VoiceCardListResponse> searchVoiceCardListResponseList = new ArrayList<>();
        for (VoiceCard vcr : voiceList) {
            searchVoiceCardListResponseList.add(
                    new VoiceCardListResponse(
                            vcr.voiceId(),
                            vcr.photoUrl(),
                            vcr.categoryInfo().getMediaClassification().getValue(),
                            vcr.categoryInfo().getVoiceTone().getValue(),
                            vcr.categoryInfo().getVoiceStyle().getValue(),
                            vcr.categoryInfo().getGender().getValue(),
                            vcr.categoryInfo().getAge().getValue(),
                            vcr.title(),
                            vcr.recordUrl(),
                            vcr.imageUrl(),
                            vcr.name(),
                            vcr.likes()
                    )
            );
        }
        return searchVoiceCardListResponseList;
    }

    @Transactional
    public void delete(Long memberId) {
        Voice voice = voiceFindService.findByMemberId(memberId);

        if(voice.getImageUrl() != null)
            fileService.deleteFiles(voice.getImageUrl());

        voiceRepository.delete(voice);
    }

    private void validateMember(Long voiceId, Long memberId) {
        Voice voice = voiceFindService.findById(voiceId);
        if (!voice.getMember().getId().equals(memberId)) {
            throw BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER);
        }
    }
}

