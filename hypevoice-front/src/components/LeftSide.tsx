import React from "react";
import styled from "styled-components";

const LeftSideDiv = styled.div`
  height: 90vh;
  width: 10%;
  background-color: #f5f5f5;
  float: left;
`;

function LeftSide() {
  return <LeftSideDiv>Left Side</LeftSideDiv>;
}

export default LeftSide;
