import { useLocation } from "react-router-dom";
import styled from "styled-components";
import MyLikeVoices from "./MyLikeVoices";
import ScrollNavigation from "./ScrollNavigation";

const RightSideDiv = styled.div`
  height: 90vh;
  width: 90%;
  background-color: white;
  float: center;
`;

const MyLikeVoiceseDiv = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

function RightSide() {
  const location = useLocation();

  return (
    <>
      <RightSideDiv>
        {location.pathname === "/" && <MyLikeVoiceseDiv><MyLikeVoices /></MyLikeVoiceseDiv>}
        {location.pathname === "/voice" && <ScrollNavigation />}
      </RightSideDiv>
    </>
  );
}

export default RightSide;
