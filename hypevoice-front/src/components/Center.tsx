import React from "react";
import styled from "styled-components";
import HomeGrid from "./HomeGrid";
import StudioList from "./StudioList";
import Voice from "./Voice";
import { Route, Routes, useLocation } from "react-router-dom";
import SearchComponent from "./SearchComponent";

const CenterStyleDiv = styled.div`
  height: 80vh;
  width: 75%;
  background-color: #f5f5f5;
  margin: auto;
  overflow: auto;
`;

function Center({ children }) {
  // const location = useLocation();

  return (
    <CenterStyleDiv>
      {/* {location.path === "/homeGrid" ? <HomeGrid /> : <StudioList />} */}
      {children}
    </CenterStyleDiv>
  );
}

export default Center;
