import React from "react";
import { Box, AppBar, Toolbar, Button } from "@mui/material";
import LogoComponent from "../components/LogoComponent";
import styled from "styled-components";
import kakaoLogo from "../assets/kakaoIcon.png";
import naverLogo from "../assets/naverIcon.jpg";

const Container = styled.div`
  height: 80vh;
  position: relative;
  overflow: hidden;
  display: grid;
  grid-template-columns: 1fr 7fr;
  justify-items: center;
  padding: 5vh;
`;

const OuterContainer = styled.div`
  display: flex;
`;

const LogoComponentContainer = styled.div`
  align-items: absolute;
  left : 50%
  transfrom : translateX(-50%);
  align-items : center;
  display: flex;
  justify-content: center;
  width: 100%;

  @media (max-width: 768px) {
    display: none;
  }
`;

const StyledLogoComponent = styled(LogoComponent)`
  width: 10vw;
  height: 10vw;

  @media (max-width: 768px) {
    width: 20vw;
    // height: 20vw;
  }
`;

const AboutLogin = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  margin: 2em;
`;

const LoginButtonContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  margin: 2em;
`;

const KakaoLoginButton = styled.button`
  background-color: yellow;
  color: black;
  text-decoration: none;
  border-radius: 10px;
  margin-bottom: 2em;
  height: 60px; // 세로 크기 조절
  overflow: hidden; // 내용이 넘칠 경우 숨기기
  border: none; // 테두리 없애기
  display: flex;
  justify-content: space-between;
  align-items: center;
  :hover {
    opacity: 70%;
  }
  width: 100%;
`;

const NaverLoginButton = styled.button`
  background-color: #03c75a;
  color: black;
  text-decoration: none;
  border-radius: 10px;
  margin-bottom: 2em;
  height: 60px; // 세로 크기 조절
  overflow: hidden; // 내용이 넘칠 경우 숨기기
  border: none; // 테두리 없애기
  display: flex;
  justify-content: space-between;
  align-items: center;
  :hover {
    opacity: 70%;
  }
  width: 100%;
`;

const LogoContainer = styled.div`
  flex: 1;
  display: flex;
  justify-content: flex-start;
`;

const TextContainer = styled.div`
  flex: 4;
  display: flex;
  justify-content: center;
  white-space: nowrap; // 줄바꿈 없애기
`;

const Logo = styled("img")`
  width: 60px;
  height: 60px;
  margin-right: 5px;
`;

function Login() {
  return (
    <OuterContainer>
      <LogoComponentContainer>
        <StyledLogoComponent />
      </LogoComponentContainer>
      <Container>
        <div></div>
        <AboutLogin>
          <h2
            style={{
              textAlign: "center",
              display: "flex",
              flexDirection: "column",
              justifyContent: "center",
              alignItems: "center",
              whiteSpace: "balance",
            }}
          >
            HYPE VOICE에서
            <div style={{ height: "20px" }}></div>
            모든 목소리를 만나보세요!
          </h2>
          <LoginButtonContainer>
            <KakaoLoginButton>
              <LogoContainer>
                <Logo src={kakaoLogo} />
              </LogoContainer>
              <TextContainer>카카오로 로그인하기</TextContainer>
            </KakaoLoginButton>
            <NaverLoginButton>
              <LogoContainer>
                <Logo src={naverLogo} />
              </LogoContainer>
              <TextContainer>네이버로 로그인하기</TextContainer>
            </NaverLoginButton>
          </LoginButtonContainer>
        </AboutLogin>
      </Container>
    </OuterContainer>
  );
}

export default Login;
