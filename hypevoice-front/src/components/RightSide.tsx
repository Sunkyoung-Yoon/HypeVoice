import React from "react";
import styled from "styled-components";

const RightSideDiv = styled.div`
  height: 90vh;
  width: 12.5%;
  background-color: #f5f5f5;
  float: right;
`;

function RightSide() {
  return <RightSideDiv>Right Side</RightSideDiv>;
}

export default RightSide;
