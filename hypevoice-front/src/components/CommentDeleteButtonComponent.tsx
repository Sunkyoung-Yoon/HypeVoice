import { useMutation, useQueryClient } from '@tanstack/react-query';
import { Box, Button, IconButton, Modal, Typography } from '@mui/material';
import ClearIcon from '@mui/icons-material/Clear';
import { useState } from 'react';
import styled from 'styled-components';
import { axiosClient } from '@/api/axios';

const DeleteBtnStyleDiv = styled.div`
	.modal-btns {
		display: flex;
		justify-content: flex-end;
		/* align-items: end; */
		/* margin-right: 40px;
		margin-bottom: 20px; */
	}
`;

const style = {
	position: 'absolute',
	top: '50%',
	left: '50%',
	transform: 'translate(-50%, -50%)',
	width: 350,
	bgcolor: 'background.paper',
	border: '2px solid #000',
	boxShadow: 24,
	p: 4,
};

type CommentIdType = {
	id: number;
};

export default function CommentDeleteButtonComponent({
	id,
}: CommentIdType): React.ReactElement {
	const queryClient = useQueryClient();
	const [open, setOpen] = useState(false);
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

	const deleteComment = async (id: number) => {
		const token = getAccessToken();
		const headers = {
			'Authorization': `Bearer ${token}`,
		};

		try {
			await axiosClient.delete(`/api/comments/${id}`, {
				headers,
			});
		} catch (error) {
			console.error(error);
		}
	};

	const { mutate } = useMutation({
		mutationFn: deleteComment,
		onError: () => {
			console.log('deleteComment : On Error');
		},
		onSuccess: () => {
			queryClient.invalidateQueries();
			alert('삭제 성공');
			console.log('deleteComment : Success, id : ' + id);
		},
		onSettled: () => {
			console.log('deleteComment : On Settled');
		},
	});

	const deletePostHandler = (id: number) => {
		mutate(id);
	};

	const handleDelete = () => {
		deletePostHandler(id);
		setOpen(false);
	};

	const handleOpen = () => {
		setOpen(true);
	};
	const handleClose = () => {
		setOpen(false);
	};

	return (
		<DeleteBtnStyleDiv>
			<IconButton aria-label="delete" size="small" onClick={handleOpen}>
				<ClearIcon fontSize="small" />
			</IconButton>
			<Modal
				open={open}
				onClose={handleClose}
				aria-labelledby="parent-modal-title"
				aria-describedby="parent-modal-description"
			>
				<Box sx={style}>
					<Typography
						id="modal-modal-title"
						variant="h6"
						component="h2"
						textAlign="center"
					>
						정말 이 댓글을 삭제하시겠습니까?
						<div className="modal-btns">
							<Button
								className="modal-cancel"
								variant="contained"
								color="warning"
								onClick={() => setOpen(false)}
								sx={{ m: 1 }}
							>
								취소
							</Button>
							<Button
								className="modal-delete"
								variant="contained"
								color="error"
								onClick={handleDelete}
								sx={{ m: 1 }}
							>
								삭제
							</Button>
						</div>
					</Typography>
				</Box>
			</Modal>
		</DeleteBtnStyleDiv>
	);
}
