import React from "react";
import ReactDOM from "react-dom/client";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { RecoilRoot } from "recoil";
import { RouterProvider } from "react-router-dom";
import { createGlobalStyle } from "styled-components";
import NanumGothicWoff2 from "../src/assets/fonts/NanumGothic-Bold.woff2";
import routerFile from "./Router";

// 폰트 스타일 정의
const FontFace = createGlobalStyle`
  @font-face {
    font-family : "Nanum Gothic", sans-serif;
    src : url(${NanumGothicWoff2}) format('woff2');
    font-weight : 700;
    font-style : normal;
  }
`;

// 전체 공통 CSS
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

// React-Query 전역 관리 객체
const queryClient = new QueryClient();
// {
// defaultOptions: {
//   queries: {
//     staleTime: 1000 * 20,
//   },
// },
// }

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
); // index.html에 있는 id가 root 인 곳에 HTMLElemnt를 ReactDOM의 root로 만들고

// 거기에 다음과 같은 것들을 렌더링하겠다.
root.render(
  // 오류 방지 레벨 높은 리액트 // 개발 후에는 주석처리할 것! // 얘 있으면 렌더링 2번 됨! 절떄 금지!!
  // <React.StrictMode>
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
  // </React.StrictMode>
);

// 웹 어플리케이션의 퍼포먼스시간들을 분석하여 객체 형태로 보여주는 메서드
reportWebVitals();
