import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import styled from 'styled-components';
import { CreateCommentType } from './CommunityType';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { LoginState } from '@/recoil/Auth';
import AudioRecorder from './AudioRecorder';

const CommentInputStyleDiv = styled.div`
	.comment-input-component {
		width: 90%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 10px;
		margin-bottom: 40px;
		padding: 5px;
		border-radius: 5px;
	}

	/* .comment-input-id {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		margin: 20px 40px;
		font-size: x-large;
		font-weight: bold;
		text-align: left;
	} */

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
		margin-right: 40px;
		margin-top: 10px;
	}

	.comment-input-button-voice {
		margin: 5px;
	}

	.comment-input-button-confirm {
		margin: 5px;
	}
`;
const base_server_url = 'https://hypevoice.site';
const CommentInputComponent = () => {
	const [comment, setComment] = useState('');
	const [voiceCommentUrl, setVoiceCommentUrl] = useState('');
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
		const data = new FormData();
		data.append(
			'request',
			new Blob([JSON.stringify(newComment)], { type: 'application/json' }),
		);
		const headers = {
			'Content-Type': 'multipart/form-data',
			'Authorization': `Bearer ${token}`,
		};

		if (voiceCommentUrl) {
			const response = await fetch(voiceCommentUrl);
			const blob = await response.blob();
			const now = new Date();
			const filename =
				now.getFullYear() +
				('0' + (now.getMonth() + 1)).slice(-2) +
				('0' + now.getDate()).slice(-2) +
				('0' + now.getHours()).slice(-2) +
				('0' + now.getMinutes()).slice(-2) +
				('0' + now.getSeconds()).slice(-2);

			data.append('file', blob, filename);
		}

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
		if (comment.length <= 0) {
			alert('댓글 내용을 입력하세요');
			return;
		}
		onCreate(comment);
		setComment('');
	};

	const handleVoiceCommentUrl = (url: string) => {
		setVoiceCommentUrl(url);
		console.log(voiceCommentUrl);
	};

	return (
		<CommentInputStyleDiv>
			{isLogin ? (
				<div className="comment-input-component">
					{/* <div className="comment-input-id">댓글 작성하기</div> */}
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
						<AudioRecorder getAudioURL={handleVoiceCommentUrl} />
						<Button
							className="comment-input-button-confirm"
							variant="contained"
							color="primary"
							size="medium"
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
