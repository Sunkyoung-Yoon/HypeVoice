import { useLocation } from "react-router-dom";
import styled from "styled-components";
import MyLikeVoices from "./MyLikeVoices";

const RightSideDiv = styled.div`
  height: 90vh;
  width: 100%;
  background-color: #f5f5f5;
  float: center;
`;

const MyLikeVoiceseDiv = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
`

function RightSide() {
  const location = useLocation();

  return (
    <RightSideDiv>
      {/* {location.pathname === "/voice" && <MyLikeVoiceseDiv><MyLikeVoices /></MyLikeVoiceseDiv>} */}
      <MyLikeVoiceseDiv><MyLikeVoices /></MyLikeVoiceseDiv>
    </RightSideDiv>
  );
}

export default RightSide;
