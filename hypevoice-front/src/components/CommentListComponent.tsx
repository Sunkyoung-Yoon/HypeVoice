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
import { QueryClient, useQuery } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import LoadingComponent from './LoadingComponent';
import { GetCommentType } from './CommunityType';

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

const queryClient = new QueryClient();
const CommentListComponent = () => {
	const loginUserId = '';
	const [currentPage, setCurrentPage] = useState<number>(1);
	const [commentsCount, setCommentsCount] = useState<number>(0);
	const [commentsPerPage, setCommentsPerPage] = useState<number>(10);
	const param = useParams();
	const currentPostId = param.id;

	// eslint-disable-next-line react-hooks/exhaustive-deps
	let commentData: GetCommentType[] = [];

	useEffect(() => {
		setCommentsCount(commentData.length);
	}, [commentData]);

	// ▼ GetComments ▼
	// const getComments = async () => {
	// 	return await axios.get(`/api/comments/${currentPostId}`);
	// };

	// const { data, isLoading, isFetched, isFetching, isError } = useQuery({
	// 	queryKey: ['post-comments'],
	// 	queryFn: getComments,
	// 	staleTime: 60000,
	// });
	const { id } = useParams();
	const base_server_url = 'http://localhost:8080';
	const getComments = async (): Promise<GetCommentType[]> => {
		const response = await axios.get(base_server_url + `/api/comments/1`);
		return response.data.commentList;
	};

	const {
		data: boardList,
		isLoading,
		isFetched,
		isError,
	} = useQuery<GetCommentType[]>({
		queryKey: ['get-comments'],
		queryFn: getComments,
		staleTime: 1000 * 60 * 5,
	});

	if (isLoading) {
		console.log('Comments : isFetched');
		return <LoadingComponent />;
	}

	// if (isFetched) {
	// 	console.log('Comments : isFetched');
	// 	queryClient.invalidateQueries({ queryKey: ['get-comments'] });
	// }

	// if (isFetching) {
	// 	console.log('Comments : isFetching');
	// 	queryClient.invalidateQueries({ queryKey: ['comments'] });
	// 	return <LoadingComponent />;
	// }

	if (isError) {
		console.log('Comments : isError');
		return <div>Error</div>;
	}

	commentData = boardList ? boardList : [];
	console.log(commentData);
	// ▲ GetComments ▲

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
