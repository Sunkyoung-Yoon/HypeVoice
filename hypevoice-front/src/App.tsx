// import React, { useState, useEffect } from "react";
// import { Outlet, useNavigate, useLocation } from "react-router-dom";
// import axios from "axios";
// import { RecoilRoot, useRecoilState, useRecoilValue } from "recoil";
import "./App.css";
// import styled from "styled-components";
// import { LoginState } from "./recoil/Auth";
// import { loadavg } from "os";

function App() {
  return (
    <>
      <div>
        <img src="/src/assets/HYPE_VOICE_IMG.png" />
        <h1>The Develop Branch</h1>
        <h2>React with Vite and TypeScript + SWC</h2>
      </div>
    </>
  )
  // const loginState = useRecoilState(LoginState);
  // const navigation = useNavigate();
  // const location = useLocation();

  // useEffect( () => {
  //   // 로컬 이전 페이지 스택에 현재 페이지 저장
  //   localStorage.setItem("previousPage", location.pathname);

  //   // 라우팅 하는 경우의 수
  //   // 1) 로그인 상태에서 로그인 페이지 접근 시 => 기로그인 사실 alert 및 이전 페이지로
  //   // 2) 로그인 안한 상태에서 로그인 요구 서비스 시도 시 => 로그인 요구 alert 및 OK 시 로그인 창으로

  //   if (location.pathname === "/login") 
  //   {
  //     if (loginState && localStorage.getItem("accessToken")) 
  //     {
  //       console.log("이미 로그인 상태입니다.");
  //       navigation("/");
  //     }
  //   } 
  //   else if (location.pathname === "/") 
  //   {
  //     if (location.state != null) 
  //     {
  //       if (
  //         location.state.wantToGo === "/myPage" ||
  //         location.state.wantToGo === "/logout"
  //       ) 
  //       {
  //         if (window.confirm("로그인 후 이용 가능한 서비스입니다. 로그인 하시겠습니까?")) 
  //         {
  //           navigation("/login");
  //         } 
  //         else {navigation(-1);}
  //       }
  //     }
  //     else
  //     {
  //       navigation("/");
  //     }
  //   }} , [loginState, location]);

  // return (
  //   <div>
  //     <Outlet />
  //   </div>
  // );
};

export default App;