import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { RecoilRoot } from "recoil";
import { Router, RouterProvider, createBrowserRouter } from "react-router-dom";
import { styled, createGlobalStyle } from "styled-components";
import routerFile from "./Router";

const GlobalStyle = createGlobalStyle`
  * {
    margin : 0;
    padding : 0;
  }

  button {
    cursor : pointer;
  }

  @font-face {
    font-family : "Nanum Gothic", sans-serif;
    src : local(※) url(NanumGothic.woff) format(‘woff’)
    font-weight : 400;
    font-style : normal;
  }
`;

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

root.render(
  // 오류 방지 레벨 높은 리액트
  <React.StrictMode>
    {/* 전역 상태 관리용 */}
    <RecoilRoot>
      {/* 전역 폰트 설정 */}
      <GlobalStyle />
      {/* 라우터 설정 */}
      <RouterProvider router={routerFile} />
    </RecoilRoot>
  </React.StrictMode>
);

// 웹 어플리케이션의 퍼포먼스시간들을 분석하여 객체 형태로 보여주는 메서드
reportWebVitals();
