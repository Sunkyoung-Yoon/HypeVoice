package hypevoice.hypevoiceback.studio.controller;

import hypevoice.hypevoiceback.auth.exception.AuthErrorCode;
import hypevoice.hypevoiceback.common.ControllerTest;
import hypevoice.hypevoiceback.studio.dto.StudioRequest;
import hypevoice.hypevoiceback.studio.dto.StudioResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static hypevoice.hypevoiceback.fixture.StudioFixture.STUDIO_FIXTURE1;
import static hypevoice.hypevoiceback.fixture.TokenFixture.ACCESS_TOKEN;
import static hypevoice.hypevoiceback.fixture.TokenFixture.BEARER_TOKEN;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Studio [Controller Layer] -> StudioController 테스트")
public class StudioControllerTest extends ControllerTest {
    @Nested
    @DisplayName("스튜디오 등록 API [POST /api/studios]")
    class createStudio {
        private static final String BASE_URL = "/api/studios";

        @Test
        @DisplayName("Authorization Header에 AccessToken이 없으면 스튜디오 생성에 실패한다")
        void withoutAccessToken() throws Exception {
            // when
            final StudioRequest request = createStudioRequest();

            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            //then
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
        @DisplayName("스튜디오 생성에 성공한다")
        void success() throws Exception {
            // given
            doReturn(1L)
                    .when(studioService)
                    .createSession(createStudioRequest());

            // when
            final StudioRequest request = createStudioRequest();
            MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                    .post(BASE_URL)
                    .header(AUTHORIZATION, BEARER_TOKEN + ACCESS_TOKEN)
                    .contentType(APPLICATION_JSON)
                    .content(convertObjectToJson(request));

            // then
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk());
        }
    }


    private StudioRequest createStudioRequest() {
        return new StudioRequest(STUDIO_FIXTURE1.getTitle(), STUDIO_FIXTURE1.getIntro(), STUDIO_FIXTURE1.getLimitNumber(), STUDIO_FIXTURE1.getIsPublic(), STUDIO_FIXTURE1.getPassword());
    }

    private StudioResponse readStudioResponse() {
        return new StudioResponse(1L, STUDIO_FIXTURE1.getSessionId(), STUDIO_FIXTURE1.getTitle(), STUDIO_FIXTURE1.getIntro(), 1, STUDIO_FIXTURE1.getLimitNumber(), STUDIO_FIXTURE1.getIsPublic(), 0);

    }
}