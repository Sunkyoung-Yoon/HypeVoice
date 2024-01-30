import React from "react";
import HeaderBarComponent from "./HeaderBarComponent";
import styled from "styled-components";

const HeaderStyleDiv = styled.div`
  height: 10vh;
  width: 100%;
`;

function Header() {
  return <HeaderBarComponent />;
}

export default Header;
