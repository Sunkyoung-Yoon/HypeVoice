import React, { useCallback, useEffect, useRef, useState } from "react";
import { styled, keyframes } from "styled-components";
import { useRecoilState } from "recoil";
import Header from "../components/Header";
import Footer from "../components/Footer";
import LeftSide from "../components/LeftSide";
import RightSide from "../components/RightSide";
import Center from "../components/Center";
import { Route, Routes } from "react-router-dom";
import StudioList from "../components/StudioList";
import HomeGrid from "../components/HomeGrid";
import Voice from "../components/Voice";
import Community from "../components/Community";

const Container = styled.div`
  height: 110vh;
  position: static;
  overflow: hidden;
`;

function Main() {
  return (
    <Container>
      <Header />
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
          path="/Voice"
          element={
            <Center>
              <Voice />
            </Center>
          }
        />
        <Route
          path="/Community"
          element={
            <Center>
              <Community />
            </Center>
          }
        />
      </Routes>

      <Footer />
    </Container>
  );
}

export default Main;
