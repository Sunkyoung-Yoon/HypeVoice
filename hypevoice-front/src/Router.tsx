import React from "react";
import { createBrowserRouter } from "react-router-dom";
import App from "./App";
import NotFound from "./pages/NotFound";
import Login from "./pages/Login";
import Main from "./pages/Main";
import Studio from "./components/Studio";
import ErrorComponent from "./components/ErrorComponent";

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
        path: ":myPage",
        element: <Main />,
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
    ],
    errorElement: <NotFound />,
  },
]);

export default router;
