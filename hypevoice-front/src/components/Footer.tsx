import React from "react";
import styled from "styled-components";

const FooterDiv = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
  height: 25vh;
  width: 100%;
  margin-bottom: 20px;
  color: #5B5FF4;
  text-align: center;
  bottom: 0; // 추가: 푸터를 화면 하단에 고정
`;

function Footer() {
  return (
    <FooterDiv>
      <h2>
        HYPE VOICE
      </h2>
      <h5>Team. 백구</h5>
      <h5>
        {"Copyright © "}
        <a href="https://www.ssafy.com/" target="_blank">
          삼성 청년 SW 아카데미
        </a>
      </h5>
    </FooterDiv>
  );
}

export default Footer;
