import React from "react";
import styled from "styled-components";

const CenterStyleDiv = styled.div`
  height: 80vh;
  width: 75%;
  background-color: #f5f5f5;
  margin: auto;
  overflow: auto;
`;

function Center() {
  return (
    <CenterStyleDiv>
      {/* 상황에 따라 Voice 또는 MyInfo 또는 WorkGrid 또는 Studio 또는 Board가 들어감 */}
    </CenterStyleDiv>
  );
}

export default Center;
