import React, { useCallback, useEffect, useRef, useState } from "react";
import { styled, keyframes } from "styled-components";
import { useRecoilState } from "recoil";
// import Header from "../components/Header";
// import Footer from "../components/Footer";
// import LeftSide from "../components/LeftSide";
// import RightSide from "../components/RightSide";
// import Center from "../components/Center";
// import Login from "../components/Home/Login/Login";
// import Join from "../components/Home/Join/Join";
// import mainimg from "/src/assets/main/mainimg.gif";
// import HomeContent from "../components/Home/HomeContent/HomeContent";
// import Messenger from "../components/Message/Messenger";
// import { LoginModeAtom } from "../recoil/Auth";
// import Custom from "../components/Util/Custom";
// import { scrollEventState } from "../recoil/HW_Atom";
// import { ContentDiv } from "../components/Home/HomeContent/HomeContent.style";

const Container = styled.div`
  height: 450vh;
  position: relative;
  overflow: hidden;
`;

function Main() {
  return (
    <Container>
      {/* <Header />
      <LeftSide />
      <RightSide />
      <Center />
      <Footer /> */}
    </Container>
  );
}

export default Main;
