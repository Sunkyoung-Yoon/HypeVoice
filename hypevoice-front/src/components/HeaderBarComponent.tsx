import { AppBar, Toolbar, Button } from "@mui/material";
import { LoginState } from "../recoil/Auth";
import styled from "@emotion/styled";
import { useNavigate } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu"; // 햄버거 아이콘
import { useRecoilState } from "recoil";
import useRequireLogin from "../hooks/useRequireLogin";
import logo from "@/assets/HYPE_VOICE_IMG.png";
import { useEffect, useState } from "react";

const HeaderButtonComponent = styled(Button)`
  color: black;
  font-weight: 1000;
  font-size: large;
  margin-inline: 10px;
  align-self: center;
  white-space: nowrap;
  :hover {
    color: #5b5ff4;
    opacity: 70%;
  }

  @media (max-width: 750px) {
    display: none;
  }
`;

const HamburgerButtonComponent = styled(MenuIcon)`
  display: none;
  color: black;
  width: 100px;
  position: fixed;
  top: 3%;
  right: 0;

  @media (max-width: 750px) {
    display: block;
  }
`;

export const LogoImg = styled.img`
  cursor: pointer;
  transition: opacity 0.1s ease, border 0.1s ease;
  border: 0 solid transparent;
  border-radius: 15%;

  &:hover {
    opacity: 0.6;
    /* border: 2px solid #5b5ff4; */
  }
`;

export default function HeaderBarComponent() {
  // 로그인 여부
  const [loginState, setLoginState] = useRecoilState(LoginState);
  const navigation = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  // 화면 너비가 변경되었을 때 실행되는 함수
  const handleResize = (event: MediaQueryListEvent) => {
    if (event.matches) {
      // 너비가 768px 이상일 때
      setMenuOpen(false);
    }
  };

  // 컴포넌트가 마운트되었을 때와 언마운트될 때 실행되는 코드입니다.
  useEffect(() => {
    const mediaQueryList = window.matchMedia("(min-width: 768px)");
    mediaQueryList.addListener(handleResize); // 이벤트 리스너를 추가합니다.

    // 컴포넌트가 언마운트될 때 실행되는 클린업 함수입니다.
    return () => {
      mediaQueryList.removeListener(handleResize); // 이벤트 리스너를 제거합니다.
    };
  }, []); // 의존성 배열이 빈 배열이므로, 이 useEffect는 컴포넌트가 마운트되었을 때 한 번만 실행됩니다.

  const handleMenuClick = () => {
    setMenuOpen(!menuOpen);
  };

  const handleLoginState = () => {
    // 로그인 상태면 로그아웃 (확인 창 한 번 띄워서 의사 한 번 더 체크!)
    // 비로그인 상태면 로그인 페이지로 이동
    if (loginState) {
      if (window.confirm("정말로 로그아웃 하시겠습니까?")) {
        navigation("/logout");
      }
    } else {
      navigation("/login");
    }
  };

  const navigateAndCloseMenu = (path: string) => {
    navigation(path);
    setMenuOpen(false);
  };

  const checkLoginAndNavigate = useRequireLogin();

  return (
    <AppBar
      position="fixed"
      style={{
        backgroundColor: "white",
        boxShadow: "none",
      }}
    >
      <Toolbar
        variant="dense"
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          height: "100%",
          width: "97%",
        }}
      >
        <div
          id="left"
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            height: "100%",
          }}
        >
          <LogoImg
            src={logo}
            alt="HypeVoice Logo"
            onClick={() => navigation("/")}
            style={{ height: "43px", alignItems: "center", padding: "10px" }}
          />
          {loginState && (
            <HeaderButtonComponent
              variant="text"
              onClick={() => checkLoginAndNavigate("/voice")}
            >
              내 보이스
            </HeaderButtonComponent>
          )}
          <HeaderButtonComponent
            variant="text"
            onClick={() => navigation("/community")}
          >
            게시판
          </HeaderButtonComponent>
          <HeaderButtonComponent
            variant="text"
            onClick={() => navigation("/studioList")}
          >
            스튜디오
          </HeaderButtonComponent>
        </div>
        <div
          id="right"
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignContent: "center",
          }}
        >
          {loginState && (
            <HeaderButtonComponent
              variant="text"
              onClick={() => checkLoginAndNavigate("/MyPage")}
            >
              마이 페이지
            </HeaderButtonComponent>
          )}
          <HeaderButtonComponent variant="text" onClick={handleLoginState}>
            {loginState ? "로그아웃" : "로그인"}
          </HeaderButtonComponent>
          <HamburgerButtonComponent onClick={handleMenuClick} />
        </div>
        {/* /////////////////////////////////////////////////////////////////////// */}
        {menuOpen && (
          <div
            id="menu"
            style={{
              position: "absolute",
              top: "100%",
              right: 0,
              width: "30%",
              backgroundColor: "white",
              overflow: "auto",
              display: "flex",
              flexDirection: "column",
            }}
          >
            <Button
              variant="text"
              onClick={() => {
                checkLoginAndNavigate("/voice");
                setMenuOpen(false); // 메뉴를 닫기
              }}
              style={{ fontSize: "Large" }}
            >
              내 보이스
            </Button>
            <Button
              variant="text"
              onClick={() => navigateAndCloseMenu("/community")}
              style={{ fontSize: "Large" }}
            >
              게시판
            </Button>
            <Button
              variant="text"
              onClick={() => navigateAndCloseMenu("/studioList")}
              style={{ fontSize: "Large" }}
            >
              스튜디오
            </Button>
            {loginState && (
              <Button
                variant="text"
                // onClick={() => checkLoginAndNavigate("/myPage")}
                onClick={() => {
                  checkLoginAndNavigate("/myPage");
                  setMenuOpen(false); // 메뉴를 닫습니다.
                }}
                style={{ fontSize: "Large" }}
              >
                마이 페이지
              </Button>
            )}
            <Button
              variant="text"
              onClick={handleLoginState}
              style={{ fontSize: "Large" }}
            >
              {loginState ? "로그아웃" : "로그인"}
            </Button>
          </div>
        )}
      </Toolbar>
    </AppBar>
  );
}
