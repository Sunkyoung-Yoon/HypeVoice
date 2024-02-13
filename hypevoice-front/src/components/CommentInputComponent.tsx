import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import styled from 'styled-components';
import { CreateCommentType } from './CommunityType';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { LoginState } from '@/recoil/Auth';

const CommentInputStyleDiv = styled.div`
	.comment-input-component {
		width: 50%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 10px;
		margin-bottom: 40px;
		padding: 10px;
		/* border: 1px solid #cccccc; */
		background-color: #e0e0e0;
		border-radius: 5px;
		box-shadow: 2px 2px 2px;
	}

	.comment-input-id {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		margin: 20px 40px;
		font-size: x-large;
		font-weight: bold;
		text-align: left;
	}

	.comment-input-textfield {
		display: flex;
		justify-content: center;
		margin-left: auto;
		margin-right: auto;
		width: 90%;
	}

	.comment-input-textfield-box {
		display: flex;
		justify-content: center;
		width: 99%;
		font-weight: bold;
		text-align: left;
	}

	.comment-input-buttons {
		display: grid;
		grid-template-columns: repeat(2, auto);
		grid-column-gap: 10px;
		justify-content: flex-end;
		margin-right: 30px;
		margin-top: 10px;
	}

	.comment-input-button-voice {
		margin: 5px;
	}

	.comment-input-button-confirm {
		margin: 5px;
	}
`;
const base_server_url = 'http://localhost:8081';
const CommentInputComponent = () => {
	const [comment, setComment] = useState('');
	const queryClient = useQueryClient();
	const isLogin = useRecoilValue(LoginState);
	const { id } = useParams();
	const getAccessToken = () => {
		console.log(document.cookie);
		const cookies = document.cookie.split('; ');
		const accessTokenCookie = cookies.find((cookie) =>
			cookie.startsWith('access_token='),
		);

		if (accessTokenCookie) {
			const accessToken = accessTokenCookie.split('=')[1];
			return accessToken;
		}
	};
	const createComment = async (
		newComment: CreateCommentType,
	): Promise<number> => {
		const token = getAccessToken();
		console.log(token);
		const data = new FormData();

		data.append(
			'request',
			new Blob([JSON.stringify(newComment)], { type: 'application/json' }),
		);
		const headers = {
			// 'Content-Type': 'multipart/form-data',
			'Authorization': `Bearer ${token}`,
		};

		// if (file) {
		// 	form.append('file', file);
		// }

		try {
			const response = await axios.post<number>(
				base_server_url + `/api/comments/${id}`,
				data,
				{ headers },
			);
			return response.data;
		} catch (error) {
			console.error(error);
			return -1;
		}
	};

	const { data, mutate } = useMutation({
		mutationFn: createComment,

		onError: () => {
			console.log('createPost : On Error');
		},
		onSuccess: () => {
			queryClient.invalidateQueries();
			console.log('createPost : Success');
		},
		onSettled: () => {
			queryClient.invalidateQueries();
			console.log('createPost : On Settled');
		},
	});

	const createPostHandler = (newComment: CreateCommentType) => {
		console.log(newComment);
		mutate(newComment);
	};

	const onCreate = (content: string) => {
		const newComment: CreateCommentType = {
			content: content,
		};
		createPostHandler(newComment);
	};

	const handleCommentInput = (event: React.ChangeEvent<HTMLInputElement>) => {
		setComment(event.target.value);
	};
	const handleSubmit = () => {
		onCreate(comment);
		setComment('');
	};

	return (
		<CommentInputStyleDiv>
			{isLogin ? (
				<div className="comment-input-component">
					<div className="comment-input-id">댓글 작성하기</div>
					<div className="comment-input-textfield">
						<TextField
							className="comment-input-textfield-box"
							// label="댓글 작성"
							variant="outlined"
							value={comment}
							onChange={handleCommentInput}
						/>
					</div>
					<div className="comment-input-buttons">
						{/*<Button
						className="comment-input-button-voice"
						variant="contained"
						color="error"
					>
						●보이스리플</Button>*/}

						<Button
							className="comment-input-button-confirm"
							variant="contained"
							color="primary"
							onClick={handleSubmit}
						>
							작성
						</Button>
					</div>
				</div>
			) : (
				<div></div>
			)}
		</CommentInputStyleDiv>
	);
};

export default CommentInputComponent;
