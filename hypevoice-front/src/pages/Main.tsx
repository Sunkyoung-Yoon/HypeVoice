import React, { useCallback, useEffect, useRef, useState } from "react";
import { styled, keyframes } from "styled-components";
import { useRecoilState } from "recoil";
import HeaderComponent from "../components/HeaderComponent";
import Footer from "../components/Footer";
import LeftSide from "../components/LeftSide";
import RightSide from "../components/RightSide";
import Center from "../components/Center";
import { Route, Routes } from "react-router-dom";
import StudioList from "../components/StudioList";
import HomeGrid from "../components/HomeGrid";
import Voice from "../components/Voice";
import CommunityComponent from "../components/CommunityComponent";

const Container = styled.div`
  height: 100vh;
  // position: static;
`;

function Main() {
  return (
    <Container>
      <HeaderComponent />
      <LeftSide />
      <RightSide />
      <Routes>
        <Route
          path="/"
          element={
            <Center>
              <HomeGrid />
            </Center>
          }
        />
        <Route
          path="/studioList"
          element={
            <Center>
              <StudioList />
            </Center>
          }
        />
        <Route
          path="/voice"
          element={
            <Center>
              <Voice />
            </Center>
          }
        />
        <Route
          path="/community"
          element={
            <Center>
              <CommunityComponent />
            </Center>
          }
        />
      </Routes>
      <Footer />
    </Container>
  );
}

export default Main;
