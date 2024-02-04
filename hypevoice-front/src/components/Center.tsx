import React from "react";
import styled from "styled-components";
import { Route, Routes, useLocation } from "react-router-dom";
import SearchComponent from "./SearchComponent";

const CenterStyleDiv = styled.div`
  width: 75%;
  background-color: #f5f5f5;
  margin: auto;
  overflow: auto;
`;

function Center({ children }) {
  return <CenterStyleDiv>{children}</CenterStyleDiv>;
}

export default Center;
