import React from "react";
import { Box, AppBar, Toolbar, Button } from "@mui/material";
import LogoComponent from "./LogoComponent";
import { LoginState } from "../recoil/Auth";
import styled from "@emotion/styled";
import { useNavigate } from "react-router-dom";

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

export default function HeaderBarComponent() {
  const navigation = useNavigate();
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
          </div>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
