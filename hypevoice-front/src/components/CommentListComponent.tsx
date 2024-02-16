import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import {
	FormControl,
	InputLabel,
	ListItem,
	MenuItem,
	Pagination,
	Select,
} from '@mui/material';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { useParams } from 'react-router-dom';
import LoadingComponent from './LoadingComponent';
import { GetCommentType } from './CommunityType';
import { useRecoilValue } from 'recoil';
import { CurrentMemberAtom } from '@/recoil/Auth';
import CommentDeleteButtonComponent from './CommentDeleteButtonComponent';
import { axiosClient } from '@/api/axios';

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
		padding: 10px;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
		border-top: 2px solid rgba(0, 0, 0, 0.1);
	}

	.comments {
		width: 95%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 20px;
		margin-bottom: 20px;
		padding: 10px;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
		/* background-color: #ffffff; */
	}

	.comment-writerNickname {
		flex-basis: 15%;
		font-weight: bold;
		text-align: left;
		/* background-color: #ffffff; */
		/* border-radius: 10; */
	}

	.comment-content {
		flex-basis: 75%;
		text-align: left;
		margin: 0 15px;
		/* background-color: #ffffff; */
	}

	.comment-modified-date {
		flex-basis: 8%;
		text-align: right;
		font-size: 12px;
		color: #888;
		margin-right: 5px;
		/* background-color: #ffffff; */
	}

	.comment-delete-button {
		flex-basis: 2%;
		margin-left: 10px;
		background-color: #fdadad;
		border-radius: 100%;
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

	.comments-unavailable {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-left: auto;
		margin-right: auto;
		padding: 4%;
	}

	.voicereply {
		display: flex;
		height: 20px;
	}
`;

const CommentListComponent = () => {
	const queryClient = useQueryClient();
	const [currentPage, setCurrentPage] = useState<number>(1);
	const [commentsCount, setCommentsCount] = useState<number>(0);
	const [commentsPerPage, setCommentsPerPage] = useState<number>(10);
	const loginUser = useRecoilValue(CurrentMemberAtom);

	let commentData: GetCommentType[] = [];

	useEffect(() => {
		setCommentsCount(commentData.length);
	}, [commentData]);

	// ▼ GetComments ▼

	const { id } = useParams();
	const getComments = async (): Promise<GetCommentType[]> => {
		const response = await axiosClient.get(`/api/comments/${id}`);
		return response.data.commentList;
	};

	const {
		data: commentList,
		isLoading,
		isFetched,
		isError,
	} = useQuery<GetCommentType[]>({
		queryKey: [`get-comments-${id}`],
		queryFn: getComments,
		staleTime: 1000 * 60 * 5,
	});

	if (isLoading) {
		console.log('Comments : isLoading');
		return <LoadingComponent />;
	}

	if (isFetched) {
		console.log('Comments : isFetched');
	}

	if (isError) {
		console.log('Comments : isError');
		return <div>Error</div>;
	}

	commentData = commentList ? commentList : [];
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

	const commentTime = (time: string) => {
		const commentDate = new Date(time);
		const currentDate = new Date();

		if (
			commentDate.getDate() === currentDate.getDate() &&
			commentDate.getMonth() === currentDate.getMonth() &&
			commentDate.getFullYear() === currentDate.getFullYear()
		) {
			// 당일 작성된 댓글인 경우 "HH:MM:SS" 형식으로 표기
			const hours = commentDate.getHours().toString().padStart(2, '0');
			const minutes = commentDate.getMinutes().toString().padStart(2, '0');
			const seconds = commentDate.getSeconds().toString().padStart(2, '0');
			return `${hours}:${minutes}:${seconds}`;
		} else {
			// 이전 날짜에 작성된 댓글인 경우 "YY.MM.DD" 형식으로 표기
			const year = commentDate.getFullYear().toString().slice(-2);
			const month = (commentDate.getMonth() + 1).toString().padStart(2, '0');
			const day = commentDate.getDate().toString().padStart(2, '0');
			return `${year}.${month}.${day}`;
		}
	};

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
					{currentComments.length >= 1 ? (
						<div className="comments-available">
							{currentComments.map((comment) => (
								<>
									<ListItem key={comment.commentId} className="comment">
										<div className="comment-writerNickname">
											{comment.writerNickname}
										</div>
										<div className="comment-content">
											<div>
												<p>{comment.content}</p>
												{comment.voiceCommentUrl ? (
													<audio
														className="voicereply"
														controls
														src={comment.voiceCommentUrl}
													/>
												) : (
													<div></div>
												)}
											</div>
										</div>
										<div className="comment-createdDate">
											{commentTime(comment.createdDate)}
										</div>
										<div className="comment-delete-button">
											{loginUser && loginUser.memberId === comment.writerId ? (
												<CommentDeleteButtonComponent id={comment.commentId} />
											) : (
												<div></div>
											)}
										</div>
									</ListItem>
								</>
							))}
							<div className="pagination">
								<Pagination
									count={pageCount}
									page={currentPage}
									onChange={handlePageClick}
								/>
							</div>
						</div>
					) : (
						<div>
							<div className="comments-unavailable">댓글이 없습니다.</div>
						</div>
					)}
				</div>
			</div>
		</CommunityListStyleDiv>
	);
};

export default CommentListComponent;
