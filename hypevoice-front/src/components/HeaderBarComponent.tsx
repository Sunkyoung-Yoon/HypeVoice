import React, { useState } from "react";
import { Box, AppBar, Toolbar, Button } from "@mui/material";
import LogoComponent from "./LogoComponent";
import { LoginState } from "../recoil/Auth";
import styled from "@emotion/styled";
import { useNavigate } from "react-router-dom";
import MenuIcon from "@mui/icons-material/Menu"; // 햄버거 아이콘

const HeaderButtonComponent = styled(Button)`
  color: black;
  margin: 10px;
  align-self: center;
  white-space: nowrap;
  :hover {
    color: #268aff;
    opacity: 70%;
  }

  @media (max-width: 768px) {
    display: none;
  }
`;

const HamburgerButtonComponent = styled(MenuIcon)`
  display: none;
  color : black;
  width : 100px;
  position: fixed;
  top : 10px;
  right: 0;

  @media (max-width: 768px) {
    display: block;
  }
`;


export default function HeaderBarComponent() {
  const navigation = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleMenuClick = () => {
    setMenuOpen(!menuOpen);
  }

  return (
    <Box
      sx={{ flexGrow: 1 }}
      style={{
        backgroundColor: "white",
        borderBottom: "3px solid black",
      }}
    >
      <AppBar
        position="fixed"
        style={{
          backgroundColor: "white",
        }}
      >
        <Toolbar
          variant="dense"
          style={{ 
            display: "flex", 
            justifyContent: "space-between",
            alignItems : "center",
            height : "100%",
            width : "97%",
        }}
        >
          <div id="left"
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              height: "100%",
            }}
          >
            <img
              src="src/assets/HYPE_VOICE_IMG.png"
              alt="HypeVoice Logo"
              style={{ height : "50px", alignItems : "center"}}
            />
            <HeaderButtonComponent variant="text">
              내 보이스
            </HeaderButtonComponent>
            <HeaderButtonComponent variant="text">게시판</HeaderButtonComponent>
            <HeaderButtonComponent variant="text">
              스튜디오
            </HeaderButtonComponent>
          </div>
          <div id="right" style={{ display: "flex", justifyContent: "space-between", alignContent : "center" }}>
            {LoginState && (
              <HeaderButtonComponent
                variant="text"
                onClick={() =>
                  navigation("/", { state: { wantToGo: "/myPage" } })
                }
              >
                마이 페이지
              </HeaderButtonComponent>
            )}
            <HeaderButtonComponent
              variant="text"
              onClick={() =>
                LoginState ? navigation("/logout") : navigation("/login")
              }
            >
              {LoginState ? "로그아웃" : "로그인"}
            </HeaderButtonComponent>
            <HamburgerButtonComponent
              onClick={handleMenuClick}
            />
          </div>
          {menuOpen && (
            <div id="menu" style={{ position: "absolute", top: "100%", left: 0, width: "100%", backgroundColor: "white", overflow: 'auto', display: 'flex', flexDirection: 'column' }}>
             {/* <div id="menu" style={{ position: "absolute", top: "100%", left: 0, width: "100%", backgroundColor: "white" }}> */}
              <Button variant="text">
                내 보이스
              </Button>
              <Button variant="text">게시판</Button>
              <Button variant="text">
                스튜디오
              </Button>
              {LoginState && (
                <Button
                  variant="text"
                  onClick={() =>
                    navigation("/", { state: { wantToGo: "/myPage" } })
                  }
                >
                  마이 페이지
                </Button>
              )}
              <Button
                variant="text"
                onClick={() =>
                  LoginState ? navigation("/logout") : navigation("/login")
                }
              >
                {LoginState ? "로그아웃" : "로그인"}
              </Button>
            </div>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
}
