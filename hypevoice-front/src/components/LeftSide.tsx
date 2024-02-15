import styled from "styled-components";
import { useLocation } from "react-router-dom";
import ScrollNavigation from "./ScrollNavigation";
import MyLikeVoices from "./MyLikeVoices";

const LeftSideDiv = styled.div`
  height: 90vh;
  width: 100%;
  background-color: white;
  float: center;
`;

const MyLikeVoiceseDiv = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`;

function LeftSide() {
  const location = useLocation();

  return (
    <>
      {/* {location.pathname === "/voice" && <MyLikeVoiceseDiv><MyLikeVoices /></MyLikeVoiceseDiv>} */}
      {/* <MyLikeVoiceseDiv>
        <MyLikeVoices />
      </MyLikeVoiceseDiv>
      <LeftSideDiv>{location.pathname === "/voice" && <ScrollNavigation />}</LeftSideDiv> */}
    </>
  );
}

export default LeftSide;
