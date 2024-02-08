import { QueryClient, useMutation } from '@tanstack/react-query';
import axios from 'axios';
import { Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';

type PostIdType = {
	id: number;
};

const queryClient = new QueryClient();
export default function DeletePost({ id }: PostIdType): React.ReactElement {
	const navigation = useNavigate();

	const deletePost = async (id: number) => {
		return await axios.delete(`api/boards/${id}`);
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

	return (
		<Button
			variant="contained"
			color="error"
			onClick={() => deletePostHandler(id)}
		>
			삭제
		</Button>
	);
}
