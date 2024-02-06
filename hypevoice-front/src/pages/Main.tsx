import React, { useCallback, useEffect, useRef, useState } from 'react';
import { styled, keyframes } from 'styled-components';
import { useRecoilState } from 'recoil';
import HeaderComponent from '../components/HeaderComponent';
import Footer from '../components/Footer';
import LeftSide from '../components/LeftSide';
import RightSide from '../components/RightSide';
import Center from '../components/Center';
import { Route, Routes } from 'react-router-dom';
import StudioList from '../components/StudioList';
import HomeGrid from '../components/HomeGrid';
import Voice from '../components/Voice';
import CommunityComponent from '../components/CommunityComponent';
import MyInfo from '../components/MyInfo';
import PostComponent from '../components/PostComponent';
import PostWriteComponent from '../components/PostWriteComponent';

// const Container = styled.div`
//   height: 100vh;
//   // position: static;
// `;

// const Body = styled.div`
//   display: flex;
//   flex-direction: row;
//   flex-gr ow: 1;
//   overflow-x: hidden;
//   min-width: 60%;
// `;

const MainGrid = styled.div`
	display: grid;
	grid-template-rows: auto 1fr auto;
	grid-template-columns: 10% 1fr 15%;
	// height: calc(100vh - 60px);
	overflow: auto;
	grid-gap: 0;
	padding-top: 60px;
	border: none;
`;

const HeaderGrid = styled.div`
	grid-row: 1 / 2;
	grid-column: 1 / 4;
	background-color: #f5f5f5;
`;

const LeftSideGrid = styled.div`
	grid-row: 2 / 3;
	grid-column: 1 / 2;
	background-color: #f5f5f5;
`;

const CenterGrid = styled.div`
	grid-row: 2 / 3;
	grid-column: 2 / 3;
	background-color: #ffffff;
`;

const RightSideGrid = styled.div`
	grid-row: 2 / 3;
	grid-column: 3 / 4;
	background-color: #f5f5f5;
`;

const FooterGrid = styled.div`
	grid-row: 3 / 4;
	grid-column: 1 / 4;
	background-color: #f5f5f5;
`;

function Main() {
	return (
		// <Container>
		<MainGrid>
			<HeaderGrid>
				<HeaderComponent />
			</HeaderGrid>
			<LeftSideGrid>
				<LeftSide />
			</LeftSideGrid>
			<CenterGrid>
				<Routes>
					<Route
						path="/"
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
								<MyInfo />
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
								<PostWriteComponent />
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
