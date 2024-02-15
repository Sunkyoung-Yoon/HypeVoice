import { styled } from "styled-components";
import HeaderComponent from "../components/HeaderComponent";
import Footer from "../components/Footer";
import LeftSide from "../components/LeftSide";
import RightSide from "../components/RightSide";
import Center from "../components/Center";
import { Route, Routes } from "react-router-dom";
import StudioList from "../components/StudioList";
import HomeGrid from "../components/HomeGrid";
import Voice from "../components/Voice";
import CommunityComponent from "../components/CommunityComponent";
import PostComponent from "@/components/PostComponent";
import MyPage from "./MyPage";
import PostCreateComponent from "@/components/PostCreateComponent";
import PostModifyComponent from "@/components/PostModifyComponent";

const HeaderHeight = "90px";

const MainGrid = styled.div`
  display: grid;
  grid-template-rows: auto 1fr auto;
  grid-template-columns: 1fr 4fr; /* 수정된 부분 */
  overflow: auto;
  grid-gap: 0;
  padding-top: ${HeaderHeight};
  border: none;
  background-color: white;

  @media (max-width: 768px) {
    grid-template-columns: 1fr; /* 반응형 스타일링 */
  }
`;

const HeaderGrid = styled.div`
  grid-row: 1 / 2;
  grid-column: 1 / 4;
  height: ${HeaderHeight};
  background-color: white;
`;

// const LeftSideGrid = styled.div`
//   grid-row: 2 / 3;
//   grid-column: 1 / 2;
//   @media (max-width: 768px) {
//     display: none;
//   }
// `;

const CenterGrid = styled.div`
  grid-row: 1 / 3;
  grid-column: 1 / 3;
  @media (max-width: 768px) {
    grid-column: 1 / -1;
  }
`;

const RightSideGrid = styled.div`
  grid-row: 2 / 3;
  grid-column: 3 / 4;
  /* background-color: #f5f5f5; */
  @media (max-width: 768px) {
    display: none;
  }
`;

const FooterGrid = styled.div`
  grid-row: 3 / 4;
  grid-column: 1 / 3; /* 수정된 부분 */
`;

function Main() {
  return (
    // <Container>
    <MainGrid>
      <HeaderGrid>
        <HeaderComponent />
      </HeaderGrid>
      {/* <LeftSideGrid>
        <LeftSide />
      </LeftSideGrid> */}
      <CenterGrid>
        <Routes>
          <Route
            path="/*"
            element={
              <Center>
                <HomeGrid />
              </Center>
            }
          />
          <Route
            path="/myPage"
            element={
              <Center>
                <MyPage />
              </Center>
            }
          />
          <Route
            path="/studioList"
            element={
              <Center>
                <StudioList />
              </Center>
            }
          />
          <Route
            path="/voice"
            element={
              <Center>
                <Voice />
              </Center>
            }
          />
          <Route
            path="/community"
            element={
              <Center>
                <CommunityComponent />
              </Center>
            }
          />
          <Route
            path="/community/:id"
            element={
              <Center>
                <PostComponent />
              </Center>
            }
          />
          <Route
            path="/community/write"
            element={
              <Center>
                <PostCreateComponent />
              </Center>
            }
          />
          <Route
            path="/community/modify"
            element={
              <Center>
                <PostModifyComponent />
              </Center>
            }
          />
        </Routes>
      </CenterGrid>
      <RightSideGrid>
        <RightSide />
      </RightSideGrid>
      <FooterGrid>
        <Footer />
      </FooterGrid>
    </MainGrid>
    // </Container>
  );
}

export default Main;
