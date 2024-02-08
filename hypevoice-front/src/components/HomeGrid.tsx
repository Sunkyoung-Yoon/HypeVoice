import React from "react";
import styled from "styled-components";
import SearchComponent from "./SearchComponent";

const HomeGridDiv = styled.div`
  height: 90vh;
  background-color: #f5f5f5;
`;

// function onGet() {
//   const accessToken = getCookie("access_token");
//   const refreshToken = getCookie("refresh_token");
//   console.log("access_token: " + accessToken);
//   console.log("refresh_token: " + refreshToken);
// }

export default function HomeGrid() {
  console.log("홈이다!");

  return (
    <HomeGridDiv>
      <SearchComponent />
    </HomeGridDiv>
  );
}
