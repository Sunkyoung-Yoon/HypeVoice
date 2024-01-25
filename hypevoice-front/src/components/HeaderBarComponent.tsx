import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Toolbar from "@mui/material/Toolbar";
import { styled } from "@mui/material/styles";

function LogoComponent() {
  return <img src="/public/static/HypeVoiceLogo.PNG" alt="HypeVoice Logo" />;
}

const HeaderButtonComponent = styled(Button)`
  color: black;
  // background-color: red;
  margin: 10px;
  align-self: center;
  white-space: nowrap;
  :hover {
    color: #268aff;
    opacity: 70%;
  }
`;

const isLogin = () => {
  return false;
};

export default function HeaderComponent() {
  return (
    <Box
      sx={{ flexGrow: 1 }}
      style={{
        // borderRadius: 25,
        backgroundColor: "white",
        borderBottom: "1px solid black",
      }}
    >
      <AppBar
        position="static"
        style={{
          backgroundColor: "white",
        }}
      >
        <Toolbar
          variant="dense"
          style={{ display: "flex", justifyContent: "space-between" }}
        >
          <div
            style={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
            }}
          >
            <LogoComponent />
            <HeaderButtonComponent variant="text">
              내 보이스
            </HeaderButtonComponent>
            <HeaderButtonComponent variant="text">게시판</HeaderButtonComponent>
            <HeaderButtonComponent variant="text">
              스튜디오
            </HeaderButtonComponent>
          </div>
          <div style={{ display: "flex", justifyContent: "space-between" }}>
            {isLogin() && (
              <HeaderButtonComponent variant="text">
                마이 페이지
              </HeaderButtonComponent>
            )}
            <HeaderButtonComponent variant="text">
              {isLogin() ? "로그아웃" : "로그인"}
            </HeaderButtonComponent>
          </div>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
