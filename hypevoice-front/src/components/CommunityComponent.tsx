import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import {
	List,
	ListItem,
	Button,
	TextField,
	ButtonGroup,
	FormControl,
	InputLabel,
	Select,
	MenuItem,
} from '@mui/material';
import Pagination from '@mui/material/Pagination';
import styled from 'styled-components';
import LoadingComponent from './LoadingComponent';
import ScrollToTopComponent from './ScrollToTopComponent';

const CommunityStyleDiv = styled.div`
	.community-component {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 98%;
		padding: 10px;
		margin-left: auto;
		margin-right: auto;
		margin-top: 20px;
		margin-bottom: 20px;
		background-color: #f3f3f3;
		border-radius: 10px;
	}

	.community-header {
		display: flex;
		justify-content: space-between;
		width: 100%;
		margin-top: 1rem;
		padding-bottom: 1rem;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
	}

	.community-header-left {
	}

	.community-header-right {
	}

	.community-post-header {
		display: flex;
		align-items: center;
		padding: 1rem 0;
		/* margin-bottom: 1rem; */
		font-size: 120%;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
	}

	.community-post-header * {
		width: 25%;
		display: inline-block;
		text-align: center;
		margin: 0px 3px;
	}

	.community-post-header-boardid {
		flex-basis: 9%;
		/* background-color: aliceblue; */
	}

	.community-post-header-category {
		flex-basis: 9%;
		/* background-color: antiquewhite; */
	}

	.community-post-header-title {
		flex-basis: 50%;
		/* background-color: aqua; */
	}

	.community-post-header-title a {
		text-decoration: none;
		transition: color 0.3s ease;
	}

	.community-post-header-title a:hover {
	}

	.community-post-header-memberid {
		flex-basis: 14%;
		/* background-color: aquamarine; */
	}

	.community-post-header-view {
		flex-basis: 9%;
		/* background-color: azure; */
	}

	.community-post-header-date {
		flex-basis: 9%;
		/* background-color: beige; */
	}

	.pagination {
		margin-top: 1rem;
		display: flex;
		justify-content: center;
	}

	.community-post-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 100%;
		text-align: center;
		font-size: 85%;
	}

	.community-post {
		display: flex;
		align-items: center;
		padding: 1rem 0;
		margin-bottom: 0.3rem;
		border-bottom: 1px solid rgba(0, 0, 0, 0.1);
	}

	.community-post div {
		width: 25%;
		display: inline-block;
		text-align: center;
		margin: 0px 3px;
	}

	.community-post-boardid {
		flex-basis: 9%;
		/* background-color: aliceblue; */
	}

	.community-post-category {
		flex-basis: 9%;
		/* background-color: antiquewhite; */
	}

	.community-post-title {
		flex-basis: 50%;
		/* background-color: aqua; */
	}

	.community-post-title a {
		text-decoration: none;
		color: #000000;
		transition: color 0.3s ease;
	}

	.community-post-title a:hover {
		color: #007bff;
	}

	.community-post-memberid {
		flex-basis: 14%;
		/* background-color: aquamarine; */
	}

	.community-post-view {
		flex-basis: 9%;
		/* background-color: azure; */
	}

	.community-post-date {
		flex-basis: 9%;
		/* background-color: beige; */
	}

	.pagination {
		display: flex;
		justify-content: center;
		margin-top: 1rem;
	}

	.search-bar {
		display: flex;
		justify-content: center;
		align-items: center;
		margin: 20px 0px;
	}
`;

// ▼ types ▼
export interface TestPostType {
	id: number;
	userId: number;
	title: string;
	body: string;
}

export interface Post {
	board_id: number;
	member_id: number;
	title: string;
	content: string;
	view: number;
	category: string;
	created_date: string;
	modified_date: string;
}
// ▲ types ▲

const CommunityComponent: React.FC = () => {
	const [searchTermInput, setSearchTermInput] = useState('');
	const [searchTerm, setSearchTerm] = useState('');
	const [currentPage, setCurrentPage] = useState<number>(1);
	const [currentcategory, setCurrentcategory] = useState<string>('전체');
	const [postsPerPage, setPostsPerPage] = useState<number>(10);
	const navigation = useNavigate();

	// ▼ axios와 React-Query를 이용하여 외부 API에서 게시물 Get ▼
	const getAllPosts = async () => {
		return await axios.get('https://jsonplaceholder.typicode.com/posts');
	};

	const { data, isLoading, isFetching, isError } = useQuery({
		queryKey: ['posts'],
		queryFn: getAllPosts,
	});

	if (isLoading) {
		return <LoadingComponent />;
	}

	if (isFetching) {
		return <LoadingComponent />;
	}

	if (isError) {
		return <div>Error</div>;
	}

	// ▲ axios와 React-Query를 이용하여 외부 API에서 게시물 Get ▲

	// rawData : 외부 API에서 불러온 데이터를 저장하는 배열.
	// (테스트를 위해 사용하는 것으로 나중에 지워야 함)
	let rawData: TestPostType[] = [];

	// postData : Back DB에서 불러온 글들을 담을 배열
	const postData: Post[] = [];

	// 불러온 데이터를 처리하는 부분
	if (data) {
		// 데이터를 board_id를 기준으로 역순으로 정렬
		rawData = data.data.sort((a: TestPostType, b: TestPostType) => b.id - a.id);

		// ▼ 외부 API에서 받아온 테스트용 데이터를 프로젝트에 맞게 처리하는 부분 ▼
		// (나중에는 필요없음)
		rawData.map((d) => {
			let category = '자유';
			if (d.id % 4 === 1) {
				category = '피드백';
			} else if (d.id % 4 === 3) {
				category = '구인구직';
			}

			const hours24InMilliseconds = 24 * 60 * 60 * 1000;
			const now = new Date();
			const postDate = new Date(now);
			const diff = now.getTime() - postDate.getTime();
			let currentTime;
			if (diff > hours24InMilliseconds) {
				// 글 작성한지 24시간이 넘었을 경우: "YY/MM/DD" 형식
				currentTime = `${postDate.getFullYear().toString().substr(-2)}/${(
					'0' +
					(postDate.getMonth() + 1)
				).slice(-2)}/${('0' + postDate.getDate()).slice(-2)}`;
			} else {
				// 글 작성한지 24시간이 안 넘었을 경우: "HH:MM" 형식
				currentTime = `${('0' + postDate.getHours()).slice(-2)}:${(
					'0' + postDate.getMinutes()
				).slice(-2)}`;
			}

			postData.push({
				board_id: d.id,
				member_id: d.userId,
				title: d.title,
				content: d.body,
				view: Math.floor(Math.random() * 100),
				category: category,
				created_date: currentTime,
				modified_date: currentTime,
			});
		});
	}
	// ▲ 외부 API에서 받아온 데이터를 우리 프로젝트에 맞게 처리하는 부분임 ▲

	// FilteredCategoryPosts : Category에 따라 글을 출력
	// (전체, 자유, 피드백, 구인구직)
	const FilteredCategoryPosts: Post[] =
		currentcategory === '전체'
			? postData
			: postData.filter((post) => post.category === currentcategory);

	// searchedPosts : 검색 결과에 따라 글을 출력
	const searchedPosts: Post[] = searchTerm
		? FilteredCategoryPosts.filter(
				(post) =>
					post.board_id.toString().includes(searchTerm) ||
					post.member_id.toString().includes(searchTerm) ||
					post.title.includes(searchTerm) ||
					post.content.includes(searchTerm),
		  )
		: FilteredCategoryPosts;

	// Pagination에 따라 글을 출력
	const pageCount: number = Math.ceil(searchedPosts.length / postsPerPage);
	const indexOfLastPost: number = currentPage * postsPerPage;
	const indexOfFirstPost: number = indexOfLastPost - postsPerPage;
	const currentPosts: Post[] = searchedPosts.slice(
		indexOfFirstPost,
		indexOfLastPost,
	);

	// 각종 EventHandler
	const handlePageClick = (
		event: React.ChangeEvent<unknown>,
		pageNumber: number,
	): void => {
		setCurrentPage(pageNumber);
	};

	const handleButtonClick = (category: string): void => {
		setCurrentcategory(category);
		setCurrentPage(1);
	};

	const handleInputChange = (
		event: React.ChangeEvent<HTMLInputElement>,
	): void => {
		setSearchTermInput(event.target.value);
	};

	const handleSearchClick = () => {
		setSearchTerm(searchTermInput);
		setCurrentPage(1);
	};

	return (
		<CommunityStyleDiv>
			<div className="community-component">
				<div className="community-header">
					<div className="community-header-left">
						<ButtonGroup
							color="primary"
							aria-label="category buttons"
							sx={{ m: 1 }}
						>
							<Button
								onClick={() => handleButtonClick('전체')}
								variant={currentcategory === '전체' ? 'contained' : 'outlined'}
							>
								전체
							</Button>
							<Button
								onClick={() => handleButtonClick('자유')}
								variant={currentcategory === '자유' ? 'contained' : 'outlined'}
							>
								자유
							</Button>
							<Button
								onClick={() => handleButtonClick('피드백')}
								variant={
									currentcategory === '피드백' ? 'contained' : 'outlined'
								}
							>
								피드백
							</Button>
							<Button
								onClick={() => handleButtonClick('구인구직')}
								variant={
									currentcategory === '구인구직' ? 'contained' : 'outlined'
								}
							>
								구인구직
							</Button>
						</ButtonGroup>
					</div>
					<div className="community-header-right">
						<Button
							variant="contained"
							sx={{ m: 1 }}
							onClick={() => navigation('/community/write')}
						>
							글쓰기
						</Button>
						<FormControl variant="standard" sx={{ m: 1, minWidth: 90 }}>
							<InputLabel id="community-header-postsperpage">
								페이지 글 개수
							</InputLabel>
							<Select
								name="selectPostsPerPage"
								value={postsPerPage}
								onChange={(e) => {
									setPostsPerPage(Number(e.target.value));
									setCurrentPage(1);
								}}
							>
								<MenuItem value={10}>10개</MenuItem>
								<MenuItem value={20}>20개</MenuItem>
								<MenuItem value={30}>30개</MenuItem>
							</Select>
						</FormControl>
					</div>
				</div>
				<List className="community-post-container">
					<ListItem className="community-post-header">
						<div className="community-post-header-boardid">번호</div>
						<div className="community-post-header-category">분류</div>
						<div className="community-post-header-title">제목</div>
						<div className="community-post-header-memberid">작성자</div>
						<div className="community-post-header-date">작성일자</div>
						<div className="community-post-header-view">조회수</div>
					</ListItem>
					{currentPosts.map((p) => (
						<ListItem key={p.board_id} className="community-post">
							<div className="community-post-boardid">{p.board_id}</div>
							<div className="community-post-category">{p.category}</div>
							<div className="community-post-title">
								<Link
									to={`/community/${p.board_id}`}
									className="community-post-title-link"
								>
									<p>{p.title}</p>
								</Link>
							</div>
							<div className="community-post-memberid">{p.member_id}</div>
							<div className="community-post-date">{p.modified_date}</div>
							<div className="community-post-view">{p.view}</div>
						</ListItem>
					))}
				</List>
				<div className="pagination">
					<Pagination
						count={pageCount}
						page={currentPage}
						onChange={handlePageClick}
					/>
				</div>
				<div className="search-bar">
					<TextField
						id="standard-basic"
						label="검색어"
						value={searchTermInput}
						onChange={handleInputChange}
						variant="standard"
						sx={{ m: 1 }}
					/>
					<Button
						variant="contained"
						size="small"
						onClick={() => {
							handleSearchClick();
							ScrollToTopComponent();
						}}
					>
						검색
					</Button>
				</div>
			</div>
		</CommunityStyleDiv>
	);
};

export default CommunityComponent;
