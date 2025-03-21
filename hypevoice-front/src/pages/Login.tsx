import LogoComponent from "../components/LogoComponent";
import styled from "styled-components";
import kakaoLogo from "@/assets/kakaoIcon.png";
import naverLogo from "@/assets/naverIcon.jpg";
import { useRedirectionWhenLoggedIn } from "../hooks/useRedirectionWhenLoggedIn";

const OuterContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  align-content: center;
  width: 100%;
  height: 100vh;
  min-height: 300px;
  @media (max-width: 900px) {
    flex-direction: column;
    justify-content: space-around;
  }
`;

const StyledLogoComponent = styled(LogoComponent)`
  width: 300px;
  height: 300px;
  object-fit: cover;
  /* margin-inline: 100px; */
`;

const H2 = styled.h2`
  font-family: "Trebuchet MS", "Lucida Sans Unicode", "Lucida Grande",
    "Lucida Sans", Arial, sans-serif;
  font-size: 3rem;
  text-align: center;
  white-space: nowrap;
  margin-bottom: 20px;
  @media (max-height: 500px) and (max-width: 900px) {
    display: none;
  }
`;

const AboutLogin = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  align-content: center;
`;

const LoginButtonContainer = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: space-between;
  width: 100%;
`;

const KakaoLoginButton = styled.button`
  background-color: #fbe300;
  color: black;
  text-decoration: none;
  border-radius: 10px;
  margin-bottom: 2em;
  height: 70px; // 세로 크기 조절
  overflow: hidden; // 내용이 넘칠 경우 숨기기
  border: none; // 테두리 없애기
  display: flex;
  justify-content: space-between;
  align-items: center;
  :hover {
    opacity: 70%;
  }
  padding-right: 10%;
`;

const NaverLoginButton = styled.button`
  background-color: #00bf19;
  color: black;
  text-decoration: none;
  border-radius: 10px;
  margin-bottom: 2em;
  height: 70px; // 세로 크기 조절
  overflow: hidden; // 내용이 넘칠 경우 숨기기
  border: none; // 테두리 없애기
  display: flex;
  justify-content: space-between;
  align-items: center;
  :hover {
    opacity: 70%;
  }
  padding-right: 10%;
`;

const LogoContainer = styled.div`
  flex: 1;
  display: flex;
  justify-content: flex-start;
`;

const TextContainer = styled.div`
  flex: 3;
  display: flex;
  justify-content: flex-start;
  white-space: nowrap; // 줄바꿈 없애기
  font-size: 26px;
  width: 200px;
`;

const Logo = styled("img")`
  width: 50px;
  height: 50px;
  margin-right: 10px;
`;

function doNaverLogin() {
  // console.log("네이버로 로그인");
  window.location.href =
    "https://hypevoice.site/api/oauth2/authorization/naver?redirect_uri=https://hypevoice.site/after-login";
}

function doKakaoLogin() {
  // console.log("카카오로 로그인");
  window.location.href =
    "https://hypevoice.site/api/oauth2/authorization/kakao?redirect_uri=https://hypevoice.site/after-login";
}

function Login() {
  useRedirectionWhenLoggedIn();
  return (
    <OuterContainer>
      <StyledLogoComponent />
      <AboutLogin>
        <H2>
          HYPE VOICE에서 <br />
          모든 목소리를 만나보세요!
        </H2>
        <LoginButtonContainer>
          <KakaoLoginButton onClick={doKakaoLogin}>
            <LogoContainer>
              <Logo src={kakaoLogo} />
            </LogoContainer>
            <TextContainer>카카오로 로그인하기</TextContainer>
          </KakaoLoginButton>
          <NaverLoginButton onClick={doNaverLogin}>
            <LogoContainer>
              <Logo src={naverLogo} />
            </LogoContainer>
            <TextContainer>네이버로 로그인하기</TextContainer>
          </NaverLoginButton>
        </LoginButtonContainer>
      </AboutLogin>
    </OuterContainer>
  );
}

export default Login;
