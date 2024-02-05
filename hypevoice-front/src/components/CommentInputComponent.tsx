import React, { useState } from 'react';
import { TextField, Button } from '@mui/material';
import styled from 'styled-components';

interface CommentInputProps {
	onSubmit: (comment: string) => void;
	nickname: string; // 닉네임을 전달받는 prop 추가
}

const CommentInputStyleDiv = styled.div`
	.comment-input-container {
		background-color: #f5f5f5;
		width: 95%;
		padding: 10px;
		padding-bottom: 20px;
		margin-left: auto;
		margin-right: auto;
	}

	.comment-input {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 10px;
		margin-left: auto;
		margin-right: auto;
		padding-top: 20px;
		padding-bottom: 20px;
	}

	.comment-input-id {
		flex-basis: 20%;
		font-weight: bold;
		text-align: left;
	}

	.comment-input-textfield {
		flex: 1;
		margin-right: 10px;
	}

	.comment-input-buttons {
		display: grid;
		grid-template-columns: repeat(2, auto); /* 버튼 요소를 두 개의 열로 배치 */
		grid-column-gap: 10px; /* 버튼 간격 조정 */
		justify-content: flex-end;
		margin-right: 10px;
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
			<div className="comment-input-container">
				<div className="comment-input">
					<span className="comment-input-id">{nickname}</span>
					<TextField
						className="comment-input-textfield"
						label="댓글 작성"
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
