import React from "react";
import styled from "styled-components";

const FooterDiv = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 10vh;
  width: 100%;
  /* background-color: #f5f5f5; */
  margin: 70px 0px 30px 0px;
  color: #5B5FF4;
  text-align: center;
`;

function Footer() {
  return (
    <FooterDiv>
      <h2>
        HYPE VOICE
      </h2>
      <h5>by. Team 백구</h5>
      <h5>
        {"Copyright © "}
        <a href="https://www.ssafy.com/">
          삼성 청년 SW 아카데미
        </a>
      </h5>
    </FooterDiv>
  );
}

export default Footer;
