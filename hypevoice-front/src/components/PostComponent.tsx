import React, { Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button } from '@mui/material';
import styled from 'styled-components';
import CommentListComponent from './CommentListComponent';
import CommentInputComponent from './CommentInputComponent';
import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { Post, TestPostType } from './CommunityComponent';
import LoadingComponent from './LoadingComponent';

const PostStyleDiv = styled.div`
	.post-component {
		background-color: #f1f1f1;
		width: 95%;
		padding: 10px;
		margin-bottom: 20px;
		margin-left: auto;
		margin-right: auto;
		border-radius: 10px;
	}

	.post-header {
		width: 95%;
		/* border: 2px solid #bbb; */
		border-radius: 5px;
		margin-left: auto;
		margin-right: auto;
		padding-top: 5px;
		padding-bottom: 5px;
		background-color: #e0e0e0;
		box-shadow: 2px 2px 2px;
	}

	.post-header-upper {
		display: flex;
		margin-left: 2%;
		margin-right: 2%;
		margin-top: 10px;
		margin-bottom: 5px;
	}

	.post-category {
		text-align: left;
		font-weight: bold;
		font-size: 20px;
		color: #555;
		margin-right: 5px;
	}

	.post-title {
		text-align: left;
		font-weight: bold;
		font-size: 20px;
		color: #555;
	}

	.post-header-lower {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-left: 2%;
		margin-right: 2%;
		margin-top: 5px;
		margin-bottom: 10px;
	}

	.post-header-lower-divline {
		margin-left: 15px;
		margin-right: 15px;
		font-size: 14px;
		text-align: left;
		color: #c0c0c0;
	}

	.post-header-lower-left {
		display: flex;
		justify-content: space-between;
	}

	.post-memberid {
		font-size: 14px;
		text-align: left;
		color: #868686;
	}

	.post-date {
		font-size: 14px;
		text-align: left;
		color: #868686;
	}

	.post-view {
		font-size: 14px;
		text-align: left;
		color: #868686;
	}

	.post-content {
		font-size: 16px;
		color: #333;
		line-height: 1.5;
		text-align: left;
		width: 90%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 20px;
		margin-bottom: 20px;
		padding: 50px 5px;
	}

	.post-tolist-button {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		margin-left: auto;
		margin-right: 40px;
		margin-top: 30px;
		margin-bottom: 10px;
	}
`;

const PostComponent: React.FC = () => {
	const navigation = useNavigate();

	// ▼ GetComment ▼
	const post_id = useParams();
	const id = post_id.id;

	const getPost = async () => {
		return await axios.get(`https://jsonplaceholder.typicode.com/posts/${id}`);
	};

	const { data, isLoading, isError } = useQuery({
		queryKey: ['posts', id],
		queryFn: getPost,
	});

	if (isLoading) {
		return <LoadingComponent />;
	}

	if (isError) {
		return <div>게시물을 불러올 수 없습니다</div>;
	}
	// ▲ GetComment ▲

	// ▼ 외부 API에서 받아온 데이터를 우리 프로젝트에 맞게 처리하는 부분임
	// (나중에는 필요없음) ▼
	const rawData: TestPostType = data?.data;
	let category = '자유';
	if (rawData.id % 4 === 1) {
		category = '피드백';
	} else if (rawData.id % 4 === 3) {
		category = '구인구직';
	}

	const now = new Date();
	const options: Intl.DateTimeFormatOptions = {
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit',
		second: '2-digit',
		hour12: false,
		timeZone: 'Asia/Seoul',
	};

	const currentTime: string = now
		.toLocaleString('ko-KR', options)
		.replace(/,/g, '');

	const postData: Post = {
		board_id: rawData.id,
		member_id: rawData.userId,
		title: rawData.title,
		content: rawData.body,
		view: Math.floor(Math.random() * 100),
		category: category,
		created_date: currentTime,
		modified_date: currentTime,
	};
	// ▲ 외부 API에서 받아온 데이터를 우리 프로젝트에 맞게 변환한 것 ▲

	const handleSubmitComment = (comment: string) => {
		// 댓글 작성 처리 로직 (아직 미작성)
		console.log(comment);
	};

	return (
		<PostStyleDiv>
			<div className="post-component">
				<Button
					variant="contained"
					className="post-tolist-button"
					onClick={() => {
						navigation('/community');
					}}
				>
					<p>목록</p>
				</Button>
				{postData ? (
					<>
						<div className="post-header">
							<div className="post-header-upper">
								<p className="post-category">[{postData.category}]</p>
								<p className="post-title">{postData.title}</p>
							</div>
							<div className="post-header-lower">
								<div className="post-header-lower-left">
									<p className="post-memberid">작성자 : {postData.member_id}</p>
									<p className="post-header-lower-divline"> | </p>
									<p className="post-date">등록일 : {postData.modified_date}</p>
								</div>
								<div className="post-header-lower-right">
									<p className="post-view">조회수 : {postData.view}</p>
								</div>
							</div>
						</div>
						<p className="post-content">{postData.content}</p>
						<div>
							<Suspense fallback={<LoadingComponent />}>
								<CommentListComponent />
							</Suspense>
						</div>
						<div>
							<CommentInputComponent
								onSubmit={handleSubmitComment}
								nickname="2"
							/>
						</div>
					</>
				) : (
					<p>잘못된 경로입니다</p>
				)}
			</div>
		</PostStyleDiv>
	);
};

export default PostComponent;
