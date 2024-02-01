package hypevoice.hypevoiceback.work.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import hypevoice.hypevoiceback.work.dto.WorkRequest;
import hypevoice.hypevoiceback.work.dto.WorkResponse;
import hypevoice.hypevoiceback.work.exception.WorkErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static hypevoice.hypevoiceback.fixture.WorkFixture.WORK_01;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Work [Controller Layer] -> WorkController 테스트")
public class WorkControllerTest extends ControllerTest {

    @Nested
    @DisplayName("작업물 등록 API [POST /api/voices/{voiceId}/works]")
    class registerWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works";
        private static final Long VOICE_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 작업물 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final WorkRequest request = createWorkRequest();

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL, VOICE_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
                    .registerWork(anyLong(), anyLong(), any(), any(), any(), any(), any(), any(), anyInt());

            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL, VOICE_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
        void registerSuccess() throws Exception {
            // given
            doNothing()
                    .when(workService)
                    .registerWork(anyLong(), anyLong(), any(), any(), any(), any(), any(), any(), anyInt());

            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL, VOICE_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("작업물 수정 API [patch /api/voices/{voiceId}/works/{workId}]")
    class updateWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/{workId}";
        private static final Long VOICE_ID = 1L;
        private static final Long WORK_ID = 2L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 작업물 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL, VOICE_ID, WORK_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
                    .updateWork(anyLong(), anyLong(), anyLong(), any(), any(), any(), any(), any(), any(), anyInt());

            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
        void updateSuccess() throws Exception {
            // given
            doNothing()
                    .when(workService)
                    .updateWork(anyLong(), anyLong(), anyLong(), any(), any(), any(), any(), any(), any(), anyInt());


            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL, VOICE_ID , WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("작업물 삭제 API [DELETE /api/voices/{voiceId}/works/{workId}]")
    class deleteWork {
        private static final String BASE_URL = "/api/voices/{voiceId}/works/{workId}";
        private static final Long VOICE_ID = 1L;
        private static final Long WORK_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 작업물 삭제에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, VOICE_ID, WORK_ID)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, VOICE_ID , WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

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
        void deleteSuccess() throws Exception {
            // given
            doNothing()
                    .when(workService)
                    .deleteWork(anyLong(), anyLong(), anyLong());

            // when
            final WorkRequest request = createWorkRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .delete(BASE_URL, VOICE_ID, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    private WorkRequest createWorkRequest() {
        return new WorkRequest(WORK_01.getTitle(), WORK_01.getVideoLink(), WORK_01.getPhotoUrl(), WORK_01.getScriptUrl(), WORK_01.getRecordUrl(), WORK_01.getInfo(), WORK_01.getIsRep());
    }

    private WorkResponse readWorkResponse() {
        return new WorkResponse(WORK_01.getTitle(), WORK_01.getVideoLink(), WORK_01.getPhotoUrl(), WORK_01.getScriptUrl(), WORK_01.getRecordUrl(), WORK_01.getInfo(), WORK_01.getIsRep());
    }
}
