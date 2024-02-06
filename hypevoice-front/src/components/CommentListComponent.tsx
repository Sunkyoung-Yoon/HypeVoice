import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import ClearIcon from '@mui/icons-material/Clear';
import {
	FormControl,
	IconButton,
	InputLabel,
	ListItem,
	MenuItem,
	Pagination,
	Select,
} from '@mui/material';
import axios from 'axios';
import { useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import LoadingComponent from './LoadingComponent';

const CommunityListStyleDiv = styled.div`
	.comments-container {
		width: 95%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 10;
		margin-bottom: 10px;
		padding: 10px;
		padding-top: 20px;
		padding-bottom: 20px;
		background-color: #f1f1f1;
	}

	.comment {
		margin-bottom: 0.8rem;
	}

	.comments-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 95%;
		margin-left: auto;
		margin-right: auto;
		padding: 20px;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
		border-top: 2px solid rgba(0, 0, 0, 0.1);
	}

	.comments {
		margin-left: 20px;
		margin-top: 20px;
		margin-bottom: 20px;
		padding: 5px;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
		/* background-color: #ffffff; */
	}

	.comment-member-id {
		flex-basis: 15%;
		max-width: 15%;
		font-weight: bold;
		text-align: left;
		/* background-color: #ffffff; */
		/* border-radius: 10; */
	}

	.comment-content {
		flex-basis: 67%;
		max-width: 67%;
		text-align: left;
		margin: 0 15px;
		/* background-color: #ffffff; */
	}

	.comment-modified-date {
		flex-basis: 12%;
		max-width: 12%;
		text-align: center;
		font-size: 12px;
		color: #888;
		margin-right: 5px;
		/* background-color: #ffffff; */
	}

	.comment-delete-button {
		flex-basis: 4%;
		max-width: 4%;
	}

	.comments-header-counter {
		padding-left: 10px;
		font-size: x-large;
		font-weight: bold;
	}

	.comments-header-commentsperpage {
		padding-right: 10px;
	}

	.pagination {
		display: flex;
		justify-content: center;
	}
`;

type TestCommentType = {
	postId: number;
	id: number;
	name: string;
	email: string;
	body: string;
};

type Comment = {
	comment_id: number;
	board_id: number;
	member_id: string;
	content: string;
	created_date: string;
	modified_date: string;
};

const CommentListComponent = () => {
	const loginUserId = '';
	const [currentPage, setCurrentPage] = useState<number>(1);
	const [commentsCount, setCommentsCount] = useState<number>(0);
	const [commentsPerPage, setCommentsPerPage] = useState<number>(10);
	const param = useParams();
	const currentPostId = param.id;

	// eslint-disable-next-line react-hooks/exhaustive-deps
	const commentData: Comment[] = [];
	let rawData: TestCommentType[] = [];

	useEffect(() => {
		setCommentsCount(commentData.length);
	}, [commentData]);

	const getComments = async () => {
		return await axios.get(
			`https://jsonplaceholder.typicode.com/post/${currentPostId}/comments`,
		);
	};

	const { data, isLoading, isFetching, isError } = useQuery({
		queryKey: ['comments'],
		queryFn: getComments,
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

	if (data) {
		rawData = data.data.sort(
			(a: TestCommentType, b: TestCommentType) => b.id - a.id,
		);
		rawData.map((comm) => {
			const userNickname = comm.email.split('@');
			comm.email = userNickname[0];

			const hours24InMilliseconds = 24 * 60 * 60 * 1000;
			const now = new Date();
			const commentDate = new Date(now);
			const diff = now.getTime() - commentDate.getTime();
			let currentTime;
			if (diff > hours24InMilliseconds) {
				// 글 작성한지 24시간이 넘었을 경우: "YY/MM/DD" 형식
				currentTime = `${commentDate.getFullYear().toString().substr(-2)}/${(
					'0' +
					(commentDate.getMonth() + 1)
				).slice(-2)}/${('0' + commentDate.getDate()).slice(-2)}`;
			} else {
				// 글 작성한지 24시간이 안 넘었을 경우: "HH:MM" 형식
				currentTime = `${('0' + commentDate.getHours()).slice(-2)}:${(
					'0' + commentDate.getMinutes()
				).slice(-2)}`;
			}

			commentData.push({
				comment_id: comm.id,
				board_id: comm.postId,
				member_id: comm.name.slice(0, 12),
				content: comm.body,
				created_date: currentTime,
				modified_date: currentTime,
			});
		});
	}

	const pageCount: number = Math.ceil(commentsCount / commentsPerPage);

	const handlePageClick = (
		event: React.ChangeEvent<unknown>,
		pageNumber: number,
	): void => {
		setCurrentPage(pageNumber);
	};

	const indexOfLastPost: number = currentPage * commentsPerPage;
	const indexOfFirstPost: number = indexOfLastPost - commentsPerPage;
	const currentComments = commentData.slice(indexOfFirstPost, indexOfLastPost);

	return (
		<CommunityListStyleDiv>
			<div className="comments-container">
				<div className="comments-header">
					<div className="comments-header-counter">댓글 {commentsCount}개</div>
					<div className="comments-header-counter">
						<FormControl variant="standard" sx={{ m: 1, minWidth: 150 }}>
							<InputLabel className="comments-header-commentsperpage">
								페이지 댓글 개수
								<Select
									name="selectCommentsPerPage"
									value={commentsPerPage}
									onChange={(e) => {
										setCommentsPerPage(Number(e.target.value));
										setCurrentPage(1);
									}}
									sx={{ ml: 1 }}
								>
									<MenuItem value={10}>10개</MenuItem>
									<MenuItem value={20}>20개</MenuItem>
									<MenuItem value={30}>30개</MenuItem>
								</Select>
							</InputLabel>
						</FormControl>
					</div>
				</div>
				<div className="comments">
					{currentComments.map((comment) => (
						<ListItem key={comment.comment_id} className="comment">
							<div className="comment-member-id">{comment.member_id}</div>
							<div className="comment-content">{comment.content}</div>
							<div className="comment-modified-date">
								{comment.modified_date}
							</div>
							<div className="comment-delete-button">
								{loginUserId === comment.member_id ? (
									<IconButton aria-label="delete" size="small">
										<ClearIcon fontSize="small" />
									</IconButton>
								) : (
									''
								)}
							</div>
						</ListItem>
					))}
				</div>
				<div className="pagination">
					<Pagination
						count={pageCount}
						page={currentPage}
						onChange={handlePageClick}
					/>
				</div>
			</div>
		</CommunityListStyleDiv>
	);
};

export default CommentListComponent;
