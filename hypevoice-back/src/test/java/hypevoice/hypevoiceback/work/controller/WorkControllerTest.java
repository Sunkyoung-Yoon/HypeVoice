package hypevoice.hypevoiceback.work.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoRequest;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoValue;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.voice.exception.VoiceErrorCode;
import hypevoice.hypevoiceback.work.dto.WorkRequest;
import hypevoice.hypevoiceback.work.dto.WorkResponse;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.*;
import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static hypevoice.hypevoiceback.fixture.WorkFixture.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Work [Controller Layer] -> WorkController 테스트")
public class WorkControllerTest extends ControllerTest {

    private static final Long VOICE_ID = 1L;
    private static final Long WORK_ID = 2L;

    @Nested
    @DisplayName("작업물 등록 API [POST /api/voices/{voiceId}/works]")
    class registerWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 작업물 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final WorkRequest request = createWorkRequest();
            MockMultipartFile[] files = new MockMultipartFile[1];
            files[0] = new MockMultipartFile("files", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID)
                    .file(files[0])
                    .file(mockRequest)
                    .accept(APPLICATION_JSON);

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
                    );
        }

        @Test
        @DisplayName("다른 사람의 보이스에 작업물을 등록할 수 없다")
        void throwExceptionByMemberIsNotVoiceMember() throws Exception {
            // given
            doThrow(BaseException.type(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER))
                    .when(workService)
                    .registerWork(anyLong(), anyLong(), any(), any(), any(), anyInt(), any());

            // when
            final WorkRequest request = createWorkRequest();
            MockMultipartFile[] files = new MockMultipartFile[1];
            files[0] = new MockMultipartFile("files", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID)
                    .file(files[0])
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            final WorkErrorCode expectedError = WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER;
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
        @DisplayName("작업물 등록에 성공한다")
        void success() throws Exception {
            // given
            doReturn(1L)
                    .when(workService)
                    .registerWork(anyLong(), anyLong(), any(), any(), any(), anyInt(), any());

            // when
            final WorkRequest request = createWorkRequest();
            MockMultipartFile[] files = new MockMultipartFile[1];
            files[0] = new MockMultipartFile("files", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID)
                    .file(files[0])
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("작업물 수정 API [patch /api/voices/{voiceId}/works/{workId}]")
    class updateWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/{workId}";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 작업물 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final WorkRequest request = createWorkRequest();
            MockMultipartFile[] files = new MockMultipartFile[1];
            files[0] = new MockMultipartFile("files", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID, WORK_ID)
                    .file(files[0])
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
                    );
        }

        @Test
        @DisplayName("다른 사람의 작업물을 수정할 수 없다")
        void throwExceptionByMemberIsNotVoiceMember() throws Exception {
            // given
            doThrow(BaseException.type(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER))
                    .when(workService)
                    .updateWork(anyLong(), anyLong(), anyLong(), any(), any(), any(), anyInt(), any());

            // when
            final WorkRequest request = createWorkRequest();
            MockMultipartFile[] files = new MockMultipartFile[1];
            files[0] = new MockMultipartFile("files", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID, WORK_ID)
                    .file(files[0])
                    .file(mockRequest)
                    .accept(APPLICATION_JSON)
                    .with(request1 -> {
                        request1.setMethod("PATCH");
                        return request1;
                    })
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            final WorkErrorCode expectedError = WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER;
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
        @DisplayName("작업물 수정에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(workService)
                    .updateWork(anyLong(), anyLong(), anyLong(), any(), any(), any(), anyInt(), any());


            // when
            final WorkRequest request = createWorkRequest();
            MockMultipartFile[] files = new MockMultipartFile[1];
            files[0] = new MockMultipartFile("files", null,
                    "multipart/form-data", new byte[]{});
            MockMultipartFile mockRequest = new MockMultipartFile("request", null,
                    "application/json", convertObjectToJson(request).getBytes(StandardCharsets.UTF_8));
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .multipart(BASE_URL, VOICE_ID, WORK_ID)
                    .file(files[0])
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
    @DisplayName("작업물 삭제 API [DELETE /api/voices/{voiceId}/works/{workId}]")
    class deleteWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/{workId}";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 작업물 삭제에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, VOICE_ID, WORK_ID);

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
                    );
        }

        @Test
        @DisplayName("다른 사람의 작업물은 삭제할 수 없다")
        void throwExceptionByMemberIsNotVoiceMember() throws Exception {
            // given
            doThrow(BaseException.type(WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER))
                    .when(workService)
                    .deleteWork(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            final WorkErrorCode expectedError = WorkErrorCode.MEMBER_IS_NOT_VOICE_MEMBER;
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
        @DisplayName("작업물 삭제에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(workService)
                    .deleteWork(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("작업물 상세조회 API [GET /api/voices/{voiceId}/works/{workId}]")
    class getDetailWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/{workId}";

        @Test
        @DisplayName("작업물 상세조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(readWorkResponse())
                    .when(workService)
                    .readWork(anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    @Nested
    @DisplayName("작업물 대표 정보 수정 API [GET /api/voices/{voiceId}/works/{workId}]")
    class updateRepresentationWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/{workId}";

        @Test
        @DisplayName("다른 사람의 작업물의 대표정보를 수정할 수 없다")
        void throwExceptionByMemberIsNotVoiceMember() throws Exception {
            // given
            doThrow(BaseException.type(VoiceErrorCode.USER_IS_NOT_VOICE_MEMBER))
                    .when(workService)
                    .updateRepresentationWork(anyLong(), anyLong(), anyLong());

            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .put(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
        @DisplayName("작업물 대표정보 수정에 성공한다.")
        void success() throws Exception {
            // given
            doNothing()
                    .when(workService)
                    .updateRepresentationWork(anyLong(), anyLong(), anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    @Nested
    @DisplayName("작업물 전체 조회 API [GET /api/voices/{voiceId}/works]")
    class readAllWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works";

        @Test
        @DisplayName("작업물 전체 조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(createWorkResponseList())
                    .when(workService)
                    .readAllWork(VOICE_ID);

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, VOICE_ID);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("카테고리를 이용한 작업물 조회 API [GET /api/voices/{voiceId}/works/filter]")
    class filterWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/filter";

        @Test
        @DisplayName("카테고리를 이용한 작업물 전체 조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(createWorkResponseList())
                    .when(workService)
                    .readCategoryWork(anyLong(), any(), any(), any(), any(), any());

            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, VOICE_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    private WorkRequest createWorkRequest() {
        return new WorkRequest(WORK_01.getTitle(), WORK_01.getVideoLink(), WORK_01.getInfo(), WORK_01.getIsRep(), createCategoryInfoRequest());
    }

    private WorkResponse readWorkResponse() {
        return new WorkResponse(VOICE_ID, WORK_ID, WORK_01.getTitle(), WORK_01.getVideoLink(), WORK_01.getPhotoUrl(), WORK_01.getScriptUrl(), WORK_01.getRecordUrl(), WORK_01.getInfo(), WORK_01.getIsRep(), createCategoryInfoValue());
    }

    private String readScriptResponse() {
        return WORK_01.getScriptUrl();
    }

    private String readVideoResponse() {
        return WORK_01.getVideoLink();
    }

    private CategoryInfoRequest createCategoryInfoRequest() {
        return new CategoryInfoRequest(CATEGORY_INFO_01.getMediaClassification().getValue(), CATEGORY_INFO_01.getVoiceTone().getValue(), CATEGORY_INFO_01.getVoiceStyle().getValue(), CATEGORY_INFO_01.getGender().getValue(), CATEGORY_INFO_01.getAge().getValue());
    }

    private CategoryInfoValue createCategoryInfoValue() {
        return new CategoryInfoValue(WORK_ID, CATEGORY_INFO_01.getMediaClassification().getValue(), CATEGORY_INFO_01.getVoiceTone().getValue(), CATEGORY_INFO_01.getVoiceStyle().getValue(), CATEGORY_INFO_01.getGender().getValue(), CATEGORY_INFO_01.getAge().getValue());
    }

    private List<WorkResponse> createWorkResponseList() {
        List<WorkResponse> workResponseList = new ArrayList<>();
        CategoryInfoValue categoryInfoValue1 = new CategoryInfoValue(1L, CATEGORY_INFO_01.getMediaClassification().getValue(), CATEGORY_INFO_01.getVoiceTone().getValue(), CATEGORY_INFO_01.getVoiceStyle().getValue(), CATEGORY_INFO_01.getGender().getValue(), CATEGORY_INFO_01.getAge().getValue());
        CategoryInfoValue categoryInfoValue2 = new CategoryInfoValue(2L, CATEGORY_INFO_02.getMediaClassification().getValue(), CATEGORY_INFO_02.getVoiceTone().getValue(), CATEGORY_INFO_02.getVoiceStyle().getValue(), CATEGORY_INFO_02.getGender().getValue(), CATEGORY_INFO_02.getAge().getValue());
        CategoryInfoValue categoryInfoValue3 = new CategoryInfoValue(3L, CATEGORY_INFO_03.getMediaClassification().getValue(), CATEGORY_INFO_03.getVoiceTone().getValue(), CATEGORY_INFO_03.getVoiceStyle().getValue(), CATEGORY_INFO_03.getGender().getValue(), CATEGORY_INFO_03.getAge().getValue());
        workResponseList.add(new WorkResponse(1L, 1L, WORK_01.getTitle(), WORK_01.getVideoLink(), WORK_01.getPhotoUrl(), WORK_01.getScriptUrl(), WORK_01.getScriptUrl(), WORK_01.getInfo(), WORK_01.getIsRep(), categoryInfoValue1));
        workResponseList.add(new WorkResponse(1L, 2L, WORK_02.getTitle(), WORK_02.getVideoLink(), WORK_02.getPhotoUrl(), WORK_02.getScriptUrl(), WORK_02.getScriptUrl(), WORK_02.getInfo(), WORK_02.getIsRep(), categoryInfoValue2));
        workResponseList.add(new WorkResponse(1L, 3L, WORK_03.getTitle(), WORK_03.getVideoLink(), WORK_03.getPhotoUrl(), WORK_03.getScriptUrl(), WORK_03.getScriptUrl(), WORK_03.getInfo(), WORK_03.getIsRep(), categoryInfoValue3));

        return workResponseList;
    }
}
