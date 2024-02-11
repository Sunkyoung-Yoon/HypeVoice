import React, { useState } from 'react';
import { QueryClient, useQuery, useQueryClient } from '@tanstack/react-query';
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
import { GetPostType } from './CommunityType';
import { useRecoilValue } from 'recoil';
import { LoginState } from '@/recoil/Auth';

const CommunityStyleDiv = styled.div`
	.community-component {
		display: flex;
		flex-direction: column;
		align-items: center;
		width: 95%;
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

	.community-post-header-writerNickname {
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

	.community-post-writerNickname {
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

// type

const base_server_url = 'http://localhost:8080';
const getAllPosts = async (): Promise<GetPostType[]> => {
	const response = await axios.get<{ boardList: GetPostType[] }>(
		base_server_url + '/api/boards',
	);
	return response.data.boardList;
};

const CommunityComponent: React.FC = () => {
	const [searchTermInput, setSearchTermInput] = useState('');
	const [searchTerm, setSearchTerm] = useState('');
	const [currentPage, setCurrentPage] = useState<number>(1);
	const [currentcategory, setCurrentcategory] = useState<string>('전체');
	const [postsPerPage, setPostsPerPage] = useState<number>(10);
	const isLogin = useRecoilValue(LoginState);
	const navigation = useNavigate();
	const queryClient = useQueryClient();
	const {
		data: boardList,
		isLoading,
		isFetched,
		isFetching,
		isError,
	} = useQuery<GetPostType[]>({
		queryKey: ['get-posts'],
		queryFn: getAllPosts,
		staleTime: 100000,
	});

	console.log(boardList);

	if (isLoading) {
		console.log('Community : isLoading');
		return <LoadingComponent />;
	}

	if (isFetched) {
		// queryClient.invalidateQueries();
		console.log('Community : isFetched');
	}

	if (isFetching) {
		console.log('Community : isFetching');
	}

	if (isError) {
		console.log('Community : isError');
		return <div>Error</div>;
	}
	const getCurrentTime = (): string => {
		const now = new Date();
		const hours = now.getHours().toString().padStart(2, '0');
		const minutes = now.getMinutes().toString().padStart(2, '0');
		return `${hours}:${minutes}`;
	};

	const formatDate = (dateString: string): string => {
		const currentTime = getCurrentTime();
		const currentDate = new Date().toISOString().split('T')[0];
		const postDate = dateString.split('T')[0];

		if (currentDate === postDate) {
			return currentTime;
		} else {
			const [year, month, day] = postDate.split('-');
			return `${year.slice(2)}.${month}.${day}`;
		}
	};

	// FilteredCategoryPosts : Category에 따라 글을 출력
	// (전체, 자유, 피드백, 구인구직)
	const FilteredCategoryPosts: GetPostType[] =
		currentcategory === '전체'
			? boardList
			: boardList.filter((post) => post.category === currentcategory);

	// searchedPosts : 검색 결과에 따라 글을 출력
	const searchedPosts: GetPostType[] = searchTerm
		? FilteredCategoryPosts.filter((post) => post.title.includes(searchTerm))
		: FilteredCategoryPosts;

	// Pagination에 따라 글을 출력
	const pageCount: number = Math.ceil(searchedPosts.length / postsPerPage);
	const indexOfLastPost: number = currentPage * postsPerPage;
	const indexOfFirstPost: number = indexOfLastPost - postsPerPage;
	const currentPosts: GetPostType[] = searchedPosts.slice(
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
						{isLogin ? (
							<Button
								variant="contained"
								sx={{ m: 1 }}
								onClick={() => navigation('/community/write')}
							>
								글쓰기
							</Button>
						) : (
							<div></div>
						)}
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
				{currentPosts ? (
					<>
						<List className="community-post-container">
							<ListItem className="community-post-header">
								<div className="community-post-header-boardid">번호</div>
								<div className="community-post-header-category">분류</div>
								<div className="community-post-header-title">제목</div>
								<div className="community-post-header-writerNickname">
									작성자
								</div>
								<div className="community-post-header-date">작성일자</div>
								<div className="community-post-header-view">조회수</div>
							</ListItem>
							{currentPosts.map((p) => (
								<ListItem key={p.boardId} className="community-post">
									<div className="community-post-boardid">{p.boardId}</div>
									<div className="community-post-category">{p.category}</div>
									<div className="community-post-title">
										<Link
											to={`/community/${p.boardId}`}
											className="community-post-title-link"
										>
											<p>{p.title}</p>
										</Link>
									</div>
									<div className="community-post-writerNickname">
										{p.writerNickname}
									</div>
									<div className="community-post-date">
										{formatDate(p.createdDate)}
									</div>
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
					</>
				) : (
					<div>게시물이 없습니다</div>
				)}
			</div>
		</CommunityStyleDiv>
	);
};

export default CommunityComponent;
