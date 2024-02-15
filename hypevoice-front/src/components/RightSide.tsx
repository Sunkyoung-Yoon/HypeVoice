import { useLocation } from "react-router-dom";
import styled from "styled-components";
import MyLikeVoices from "./MyLikeVoices";
import { LoginState } from "@/recoil/Auth";
import { useRecoilValue } from "recoil";

const RightSideDiv = styled.div`
  height: 90vh;
  width: 100%;
  background-color: #f5f5f5;
  float: center;
`;

const MyLikeVoiceseDiv = styled.div`
position: fixed;
  top: 16%;
  right: 30px;
  text-align: center;

  display: flex;
  justify-content: center;
  align-items: center;
`

function RightSide() {
  const location = useLocation();
  const loginState = useRecoilValue(LoginState);

  return (
    <>
      <RightSideDiv>
        {loginState && location.pathname === "/" && <MyLikeVoiceseDiv><MyLikeVoices /></MyLikeVoiceseDiv>}
        {location.pathname === "/voice" && <MyLikeVoiceseDiv><MyLikeVoices /></MyLikeVoiceseDiv>}
      </RightSideDiv>
    </>
  );
}

export default RightSide;
