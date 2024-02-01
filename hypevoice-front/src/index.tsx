import React from "react";
import ReactDOM from "react-dom/client";
// import { QueryClient, QueryClientProvider } from "react-query";
import {
  QueryClient,
  QueryClientProvider,
  useQuery,
} from "@tanstack/react-query";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import { RecoilRoot } from "recoil";
import { Router, RouterProvider, createBrowserRouter } from "react-router-dom";
import { styled, createGlobalStyle } from "styled-components";
import NanumGothicWoff2 from "../src/assets/fonts/NanumGothic-Bold.woff2";
import routerFile from "./Router";

const FontFace = createGlobalStyle`
  @font-face {
    font-family : "Nanum Gothic", sans-serif;
    src : url(${NanumGothicWoff2}) format('woff2');
    font-weight : 700;
    font-style : normal;
  }
`;

const GlobalStyle = createGlobalStyle`
  * {
    margin : 0;
    padding : 0;
  }

  body {
    font-family : "Nanum Gothic", sans-serif;
  }

  button {
    cursor : pointer;
  }

`;

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

root.render(
  // 오류 방지 레벨 높은 리액트
  <React.StrictMode>
    {/* <style>
      @import
      url('https://fonts.googleapis.com/css2?family=Nanum+Gothic:wght@700&display=swap')
    </style> */}
    <QueryClientProvider client={queryClient}>
      {/* 전역 상태 관리용 */}
      <RecoilRoot>
        {/* 전역 폰트 & 스타일 설정 */}
        <FontFace />
        <GlobalStyle />
        {/* 라우터 설정 */}
        <RouterProvider router={routerFile} />
      </RecoilRoot>
    </QueryClientProvider>
  </React.StrictMode>
);

// 웹 어플리케이션의 퍼포먼스시간들을 분석하여 객체 형태로 보여주는 메서드
reportWebVitals();
