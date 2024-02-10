import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import NotFound from "./pages/NotFound";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Studio from "./components/Studio";
import ErrorComponent from "./components/ErrorComponent";
import StudioList from "./components/StudioList";
import HomeGrid from "./components/HomeGrid";
import Voice from "./components/Voice";
import CommunityComponent from "./components/CommunityComponent";
import path from "path";
import KakaoLogin from "./pages/KakaoLogin";
import NaverLogin from "./pages/NaverLogin";
import PostComponent from "./components/PostComponent";
import MyPage from "./pages/MyPage";
import PostCreateComponent from "./components/PostCreateComponent";
import AfterLogin from "./pages/AfterLogin";

const router = createBrowserRouter([
  {
    path: "/*",
    element: <App />, // App 은 UMD 여서 React import 필요!
    children: [
      {
        path: "",
        element: <Main />,
        children: [
          {
            path: "myPage",
            element: <MyPage />,
            errorElement: <ErrorComponent />,
          },
          {
            path: "studioList",
            element: <StudioList />,
            errorElement: <ErrorComponent />,
          },
          {
            path: "homeGrid",
            element: <HomeGrid />,
            errorElement: <ErrorComponent />,
          },
          {
            path: "voice",
            element: <Voice />,
            errorElement: <ErrorComponent />,
          },
          {
            path: "community",
            element: <CommunityComponent />,
            errorElement: <ErrorComponent />,
          },
          {
            path: 'community/:id',
            element: <PostComponent />,
            errorElement: <ErrorComponent />,
          },
          {
            path: 'community/write',
            element: <PostCreateComponent />,
            errorElement: <ErrorComponent />,
          },
        ],
        errorElement: <NotFound />,
      },
    ],
    errorElement: <ErrorComponent />,
  },
  {
    path: "after-login",
    element: <AfterLogin />,
    errorElement: <ErrorComponent />,
  },
  {
    path: "login",
    element: <Login />,
    errorElement: <ErrorComponent />,
  },
  {
    path: "studio",
    element: <Studio />,
    errorElement: <ErrorComponent />,
  },
]);

export default router;
