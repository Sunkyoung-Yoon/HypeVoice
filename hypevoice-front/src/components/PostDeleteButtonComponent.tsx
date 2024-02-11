import { QueryClient, useMutation } from '@tanstack/react-query';
import axios from 'axios';
import { Box, Button, Modal, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import styled from 'styled-components';

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
	width: 250,
	bgcolor: 'background.paper',
	border: '2px solid #000',
	boxShadow: 24,
	p: 4,
};

type PostIdType = {
	id: number;
};

const queryClient = new QueryClient();
const base_server_url = 'http://localhost:8080';
export default function PostDeleteButtonComponent({
	id,
}: PostIdType): React.ReactElement {
	const navigation = useNavigate();
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
	// const deletePost = async (id: number) => {
	// 	return await axios.delete(`api/boards/${id}`);
	// };

	const deletePost = async (id: number) => {
		const token = getAccessToken();
		const headers = {
			'Authorization': `Bearer ${token}`,
		};

		try {
			await axios.delete(base_server_url + `/api/boards/${id}`, {
				headers,
			});
		} catch (error) {
			console.error(error);
		}
	};

	const { mutate } = useMutation({
		mutationFn: deletePost,
		onError: () => {
			console.log('deletePost : On Error');
		},
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: ['community-posts'] });
			navigation('/community');
			alert('삭제 성공');
			console.log('deletePost : Success, id : ' + id);
		},
		onSettled: () => {
			console.log('deletePost : On Settled');
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
			<Button variant="contained" color="error" onClick={handleOpen}>
				삭제
			</Button>
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
						정말 삭제하시겠습니까?
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
