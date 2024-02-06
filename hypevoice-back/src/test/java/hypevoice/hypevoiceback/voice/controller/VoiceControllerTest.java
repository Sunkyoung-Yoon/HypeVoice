package hypevoice.hypevoiceback.voice.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
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
        private  static final String NAME = "NAME";

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
    class getDetailVoice {
        private static final String BASE_URL = "/api/voices/{voiceId}";
        private static final Long VOICE_ID = 1L;

        @Test
        @DisplayName("보이스 상세조회에 성공한다")
        void readSuccess() throws Exception {
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

    private VoiceUpdateRequest updateRequest(){
        return new VoiceUpdateRequest(updateVoice.NAME,null,null,null,null);
    }

    private VoiceReadResponse readResponse(){
        return new VoiceReadResponse(1L, updateVoice.NAME,null,"intro","email","phone","addInfo",109);
    }
}
