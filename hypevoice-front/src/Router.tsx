import React from "react";
import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import NotFound from "./pages/NotFound";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Studio from "./components/Studio";
import MyInfo from "./components/MyInfo";
import ErrorComponent from "./components/ErrorComponent";
import StudioList from "./components/StudioList";
import HomeGrid from "./components/HomeGrid";
import Voice from "./components/Voice";
import CommunityComponent from "./components/CommunityComponent";

const router = createBrowserRouter([
  {
    path: "/*",
    element: <App />, // App 은 UMD 여서 React import 필요!
    children: [
      {
        path: "",
        element: <Main />,
        errorElement: <ErrorComponent />,
      },
      {
        path: "myPage",
        element: <Main />,
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
    ],
    errorElement: <NotFound />,
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
