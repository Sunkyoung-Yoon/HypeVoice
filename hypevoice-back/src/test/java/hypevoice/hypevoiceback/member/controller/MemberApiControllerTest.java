package hypevoice.hypevoiceback.member.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.member.dto.MemberUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Member [Controller Layer] -> MemberApiController 테스트")
public class MemberApiControllerTest extends ControllerTest {

    @Nested
    @DisplayName("회원 정보 수정 API [PATCH /api/members]")
    class updateBoard {
        private static final String BASE_URL = "/api/members";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 회원 정보 수정에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final MemberUpdateRequest request = updateMemberRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL)
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
        @DisplayName("회원 정보 수정에 성공한다")
        void success() throws Exception {
            // given
            doNothing()
                    .when(memberService)
                    .update(anyLong(), any(), any());

            // when
            final MemberUpdateRequest request = updateMemberRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .patch(BASE_URL)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }

//    @Nested
//    @DisplayName("게시글 상세조회 API [GET /api/boards/{boardId}]")
//    class getDetailBoard {
//        private static final String BASE_URL = "/api/boards/{boardId}";
//        private static final Long BOARD_ID = 2L;
//
//        @Test
//        @DisplayName("게시글 상세조회에 성공한다")
//        void success() throws Exception {
//            // given
//            doReturn(readBoardResponse())
//                    .when(boardService)
//                    .read(anyLong());
//
//            // when
//            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                    .get(BASE_URL, BOARD_ID)
//                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN);
//
//            // then
//            mockMvc.perform(requestBuilder)
//                    .andExpectAll(status().isOk());
//        }
//    }

    private MemberUpdateRequest updateMemberRequest() {
        return new MemberUpdateRequest("voice123", "프로필이미지Url");
    }
}
