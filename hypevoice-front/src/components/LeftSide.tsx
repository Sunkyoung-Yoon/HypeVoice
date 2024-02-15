import styled from "styled-components";
import { useLocation } from "react-router-dom";
import ScrollNavigation from "./ScrollNavigation";

const LeftSideDiv = styled.div`
  height: 90vh;
  width: 100%;
  background-color: #f5f5f5;
  float: center;
`;

function LeftSide() {
  const location = useLocation();

  return (
    <>
      <LeftSideDiv>{location.pathname === "/voice" && <ScrollNavigation />}</LeftSideDiv>
    </>
  );
}

export default LeftSide;
