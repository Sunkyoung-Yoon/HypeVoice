import React, { useState, useEffect } from "react";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import { RecoilRoot, useRecoilState, useRecoilValue } from "recoil";
import styled from "styled-components";
import { LoginState } from "./recoil/Auth";
import { loadavg } from "os";
import "./App.css";
import Main from "./pages/Main";

function App() {
  const loginState = useRecoilValue(LoginState);
  const navigation = useNavigate();
  const location = useLocation();

  return <Main />;
}

export default App;
