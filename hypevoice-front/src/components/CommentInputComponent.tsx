import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import styled from 'styled-components';
import { CommentInputProps } from './CommunityType';

const CommentInputStyleDiv = styled.div`
	.comment-input-component {
		width: 80%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 10;
		margin-bottom: 40px;
		padding: 10px;
		padding-top: 20px;
		padding-bottom: 20px;
		/* border: 1px solid #cccccc; */
		background-color: #e0e0e0;
		border-radius: 5px;
		box-shadow: 2px 2px 2px;
	}

	.comment-input-id {
		display: flex;
		align-items: center;
		justify-content: flex-start;
		margin-left: 6%;
		margin-bottom: 3%;
		font-size: large;
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
		margin-right: 6%;
		margin-top: 2%;
	}

	.comment-input-button-voice {
		margin: 5px;
	}

	.comment-input-button-confirm {
		margin: 5px;
	}
`;

const CommentInputComponent: React.FC<CommentInputProps> = ({
	onSubmit,
	nickname,
}) => {
	const [comment, setComment] = useState('');

	const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setComment(event.target.value);
	};

	const handleSubmit = () => {
		if (comment.trim() === '') {
			return;
		}
		onSubmit(comment);
		setComment('');
	};

	return (
		<CommentInputStyleDiv>
			<div className="comment-input-component">
				<div className="comment-input-id">
					<p>댓글 작성하기</p>
				</div>
				<div className="comment-input-textfield">
					<TextField
						className="comment-input-textfield-box"
						// label="댓글 작성"
						variant="outlined"
						value={comment}
						onChange={handleInputChange}
					/>
				</div>
				<div className="comment-input-buttons">
					<Button
						className="comment-input-button-voice"
						variant="contained"
						color="error"
					>
						●보이스리플
					</Button>
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
		</CommentInputStyleDiv>
	);
};

export default CommentInputComponent;
