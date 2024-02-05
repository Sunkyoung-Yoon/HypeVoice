package hypevoice.hypevoiceback.categoryInfo.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoRequest;
import hypevoice.hypevoiceback.categoryInfo.dto.CategoryInfoResponse;
import hypevoice.hypevoiceback.categoryInfo.exception.CategoryInfoErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.global.exception.BaseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hypevoice.hypevoiceback.fixture.CategoryInfoFixture.CATEGORY_INFO_01;
import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CategoryInfo [Controller Layer] -> CategoryInfoController 테스트")

public class CategoryInfoControllerTest extends ControllerTest {

    @Nested
    @DisplayName("카테고리 등록 API [POST /api/works/{workId}/categoryInfos]")
    class createCategoryInfo {
        private static final String BASE_URL = "/api/works/{workId}/categoryInfos";
        private static final Long WORK_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 카테고리 정보 등록에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL, WORK_ID)
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
        @DisplayName("카테고리 정보 저장에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(categoryInfoService)
                    .createCategoryInfo(anyLong(), anyLong(), any(), any(), any(), any(), any());

            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("카테고리 수정 API [PATCH /api/works/{workId}/categoryInfos]")
    class updateCategoryInfo {
        private static final String BASE_URL = "/api/works/{workId}/categoryInfos";
        private static final Long WORK_ID = 1L;

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 카테고리 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL, WORK_ID)
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
        @DisplayName("다른 사람의 작업물의 카테고리는 수정할 수 없다")
        void throwExceptionByMemberIsNotWorkMember() throws Exception {
            // given
            doThrow(BaseException.type(CategoryInfoErrorCode.MEMBER_IS_NOT_WORK_MEMBER))
                    .when(categoryInfoService)
                    .updateCategoryInfo(anyLong(), anyLong(), any(), any(), any(), any(), any());

            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            final CategoryInfoErrorCode expectedError = CategoryInfoErrorCode.MEMBER_IS_NOT_WORK_MEMBER;
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
        @DisplayName("카테고리 수정에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(categoryInfoService)
                    .updateCategoryInfo(anyLong(), anyLong(), any(), any(), any(), any(), any());

            // when
            final CategoryInfoRequest request = createCategoryInfoRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL, WORK_ID)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("카테고리 정보 조회 API [GET /api/works/{workId}/categoryInfos]")
    class getDetailCategoryInfo {
        private static final String BASE_URL = "/api/works/{workId}/categoryInfos";
        private static final Long WORK_ID = 2L;

        @Test
        @DisplayName("카테고리 정보 조회에 성공한다")
        void success() throws Exception {
            // given
            doReturn(readCategoryInfoResponse())
                    .when(categoryInfoService)
                    .readCategoryInfo(anyLong());

            // when
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get(BASE_URL, WORK_ID);

            // then
            mockMvc.perform(requestBuilder)
                    .andExpectAll(status().isOk());
        }
    }

    private CategoryInfoRequest createCategoryInfoRequest() {
        return new CategoryInfoRequest(CATEGORY_INFO_01.getMediaClassification().getValue(), CATEGORY_INFO_01.getVoiceTone().getValue(), CATEGORY_INFO_01.getVoiceStyle().getValue(), CATEGORY_INFO_01.getGender().getValue(), CATEGORY_INFO_01.getAge().getValue());
    }

    private CategoryInfoResponse readCategoryInfoResponse() {
        return new CategoryInfoResponse(2L, CATEGORY_INFO_01.getMediaClassification().getValue(), CATEGORY_INFO_01.getVoiceTone().getValue(), CATEGORY_INFO_01.getVoiceStyle().getValue(), CATEGORY_INFO_01.getGender().getValue(), CATEGORY_INFO_01.getAge().getValue());
    }
}
