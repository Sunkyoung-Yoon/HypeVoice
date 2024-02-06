import React from "react";
import HeaderBarComponent from "./HeaderBarComponent";
import styled from "styled-components";

const HeaderStyleDiv = styled.div`
  position: fixed; /* 추가: 헤더를 화면 상단에 고정 */
  height: 1px;
  width: 100%;
  z-index: 100; /* 추가: 다른 요소들 위에 표시되도록 z-index 설정 */
`;

function HeaderComponent() {
  return (
    <HeaderStyleDiv>
      <HeaderBarComponent />
    </HeaderStyleDiv>
  );
}

export default HeaderComponent;
