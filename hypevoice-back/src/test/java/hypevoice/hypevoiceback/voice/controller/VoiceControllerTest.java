package hypevoice.hypevoiceback.voice.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoRequest;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.dto.VoiceCardListResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceReadResponse;
import hypevoice.hypevoiceback.voice.dto.VoiceUpdateRequest;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Voice [Controller Layer] -> VoiceController 테스트")
public class VoiceControllerTest extends ControllerTest {

    @Nested
    @DisplayName("보이스 수정 API [PATCH /api/voices/{voiceId}]")
    class updateVoice {
        private static final String BASE_URL = "/api/voices/{voiceId}";
        private static final Long VOICE_ID = 1L;
        private static final String NAME = "NAME";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 보이스 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final VoiceUpdateRequest request = updateRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    });

            // then
            final AuthErrorCode expectedError = AuthErrorCode.INVALID_PERMISSION;

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isForbidden(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    )
            ;
        }

        @Test
        @DisplayName("다른 사람의 보이스은 수정할 수 없다")
        void throwExceptionByUserIsNotVoiceMember() throws Exception {
            // given
            doThrow(BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER))
                    .when(voiceService)
                    .updateVoice(anyLong(), anyLong(), any(), any(), any(), any(), any(), any());

            // when
            final VoiceUpdateRequest request = updateRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    })
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            final VoiceErrorCode expectedError = VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER;
            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.status").exists(),
                            jsonPath("$.status").value(expectedError.getStatus().value()),
                            jsonPath("$.errorCode").exists(),
                            jsonPath("$.errorCode").value(expectedError.getErrorCode()),
                            jsonPath("$.message").exists(),
                            jsonPath("$.message").value(expectedError.getMessage())
                    );
        }

        @Test
        @DisplayName("보이스 수정에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(voiceService)
                    .updateVoice(anyLong(), anyLong(), any(), any(), any(), any(), any(), any());

            // when
            final VoiceUpdateRequest request = updateRequest();
            MockMultipartFile file = new MockMultipartFile("file", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID)
                    .file(file)
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    })
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("보이스 상세조회 API [GET /api/voices/{voiceId}]")
    class readDetailVoice {
        private static final String BASE_URL = "/api/voices/{voiceId}";
        private static final Long VOICE_ID = 1L;

        @Test
        @DisplayName("보이스 상세조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(readResponse())
                    .when(voiceService)
                    .readDetailVoice(anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, VOICE_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    @Nested
    @DisplayName("보이스 전체 조회 API [GET /api/voices/list/date]")
    class readAllVoice {
        private static final String BASE_URL = "/api/voices/list/date";

        @Test
        @DisplayName("보이스 전체 조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(voiceCardListResponses())
                    .when(voiceService)
                    .readAllVoice();

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    @Nested
    @DisplayName("보이스 검색 API [GET /api/voices/search]")
    class searchVoice {
        private static final String BASE_URL = "/api/voices/search";

        @Test
        @DisplayName("보이스 검색에 성공한다")
        void success() throws Exception {
            // given
            doReturn(voiceCardListResponses())
                    .when(voiceService)
                    .searchVoice(anyString());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .param("keyword", "title");

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    @Nested
    @DisplayName("카테고리를 이용한 보이스 조회 API [GET /api/voices/filter]")
    class filterVoice {
        private static final String BASE_URL = "/api/voices/filter";

        @Test
        @DisplayName("카테고리를 이용한 보이스 조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(voiceCardListResponses())
                    .when(voiceService)
                    .filterVoiceByCategory(any(), any(), any(), any(), any());

            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }


    private VoiceUpdateRequest updateRequest() {
        return new VoiceUpdateRequest(updateVoice.NAME, null, null, null, null);
    }

    private VoiceReadResponse readResponse() {
        return new VoiceReadResponse(updateVoice.NAME, null, "intro", "email", "phone", "addInfo", 109);
    }

    private List<VoiceCardListResponse> voiceCardListResponses() {
        List<VoiceCardListResponse> voiceCardListResponseList = new ArrayList<>();
        VoiceCardListResponse v1 = new VoiceCardListResponse(1L, "photo1", "M01", "T01", "S01", "G01", "A01", "title1", "record1", "image1", "name1", 100);
        VoiceCardListResponse v2 = new VoiceCardListResponse(2L, "photo2", "M02", "T02", "S02", "G02", "A02", "title2", "record2", "image2", "name2", 200);
        VoiceCardListResponse v3 = new VoiceCardListResponse(3L, "photo3", "M03", "T03", "S03", "G03", "A03", "title3", "record3", "image3", "name3", 300);
        voiceCardListResponseList.add(v1);
        voiceCardListResponseList.add(v2);
        voiceCardListResponseList.add(v3);

        return voiceCardListResponseList;
    }

    private CategoryInfoRequest createCategoryInfoRequest() {
        return new CategoryInfoRequest("M01", "T01", "S01", "G01", "A01");
    }
}
