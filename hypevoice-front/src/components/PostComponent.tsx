import React, { Suspense } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button } from '@mui/material';
import styled from 'styled-components';
import { QueryClient, useQuery } from '@tanstack/react-query';
import axios from 'axios';
import LoadingComponent from './LoadingComponent';
import PostDeleteButtonComponent from './PostDeleteButtonComponent';
import { GetPostType } from './CommunityType';
import { useRecoilValue } from 'recoil';
import { LoginState } from '@/recoil/Auth';
import CommentListComponent from './CommentListComponent';
import CommentInputComponent from './CommentInputComponent';

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

	.post-writernickname {
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
		margin-top: 10px;
		margin-bottom: 10px;
	}

	.post-footer {
		display: flex;
		justify-content: flex-end;
		align-items: center;
		margin-right: 40px;
		margin-bottom: 20px;
	}

	.post-modify-button {
		margin-right: 10px;
	}
`;

const queryClient = new QueryClient();
const base_server_url = 'http://localhost:8080';

const PostComponent: React.FC = () => {
	const navigation = useNavigate();
	const isLogin = useRecoilValue(LoginState);

	// ▼ GetPost ▼
	const { id } = useParams();

	const getPost = async (): Promise<GetPostType> => {
		const response = await axios.get(base_server_url + `/api/boards/${id}`);
		return response.data;
	};

	const { data, isLoading, isFetched, isError } = useQuery<GetPostType>({
		queryKey: ['get-post'],
		queryFn: getPost,
		staleTime: 1000 * 60 * 5, // 5 minutes
	});

	console.log(data);
	if (isLoading) {
		console.log('Post : isLoading');
		return <LoadingComponent />;
	}

	if (isFetched) {
		console.log('Post : isFetched');
		queryClient.invalidateQueries({ queryKey: ['get-post'] });
	}

	if (isError) {
		console.log('Post : isError');
		return <div>게시물을 불러올 수 없습니다</div>;
	}

	const getPostData = data;
	// ▲ GetPost ▲

	const modifiedTime = (time: string) => {
		const date = new Date(time);

		const formattedTime = date.toLocaleString('ko-KR', {
			year: 'numeric',
			month: '2-digit',
			day: '2-digit',
			hour: '2-digit',
			minute: '2-digit',
			second: '2-digit',
			hour12: false,
		});

		return formattedTime;
	};

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
						queryClient.invalidateQueries();
					}}
				>
					<p>목록</p>
				</Button>
				{getPostData ? (
					<>
						<div className="post-header">
							<div className="post-header-upper">
								<p className="post-category">[{getPostData.category}]</p>
								<p className="post-title">{getPostData.title}</p>
							</div>
							<div className="post-header-lower">
								<div className="post-header-lower-left">
									<p className="post-writernickname">
										작성자 : {getPostData.writerNickname}
									</p>
									<p className="post-header-lower-divline"> | </p>
									<p className="post-date">
										등록일 : {modifiedTime(getPostData.createdDate)}
									</p>
								</div>
								<div className="post-header-lower-right">
									<p className="post-view">조회수 : {getPostData.view}</p>
								</div>
							</div>
						</div>
						{/*{getPostData.recordUrl ? (
							<div>
								<img src={`${getPostData.recordUrl}`} />
							</div>
						) : (
							<div></div>
						)}*/}
						<p className="post-content">{getPostData.content}</p>
						<div className="post-footer">
							<Button
								variant="contained"
								color="warning"
								className="post-modify-button"
								onClick={() =>
									navigation(`/community/modify`, { state: { id: id } })
								}
							>
								<p>수정</p>
							</Button>
							<PostDeleteButtonComponent id={getPostData.boardId} />
						</div>
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
