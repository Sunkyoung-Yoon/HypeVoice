import React from "react";
import styled from "styled-components";

const CenterStyleDiv = styled.div`
  width: 100%%;
  background-color: #f5f5f5;
  // margin: auto;
  // overflow: auto;
`;

function Center({ children }) {
  return <CenterStyleDiv>{children}</CenterStyleDiv>;
}

export default Center;
