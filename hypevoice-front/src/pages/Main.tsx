import React, { useCallback, useEffect, useRef, useState } from "react";
import { styled, keyframes } from "styled-components";
import { useRecoilState } from "recoil";
import Header from "../components/Header";
import Footer from "../components/Footer";
import LeftSide from "../components/LeftSide";
import RightSide from "../components/RightSide";
import Center from "../components/Center";

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
      <Center />
      <Footer />
    </Container>
  );
}

export default Main;
