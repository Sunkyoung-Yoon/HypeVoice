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
import Community from "./components/Community";
import Voice from "./components/Voice";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />, // App 은 UMD 여서 React import 필요!
    children: [
      {
        path: "",
        element: <Main />,
        errorElement: <ErrorComponent />,
      },

      {
        path: "studio",
        element: <Studio />,
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
        element: <Community />,
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
]);

export default router;
