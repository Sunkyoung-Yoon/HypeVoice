import { Button, TextField } from '@mui/material';
import React, { useState } from 'react';
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { useLocation, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { GetPostType } from './CommunityType';
import LoadingComponent from './LoadingComponent';
import { CreatePostType } from './CommunityType';
// import AudioRecord from './AudioRecord';

const PostEditorStyleDiv = styled.div`
	.post-editor-component {
		width: 80%;
		padding: 5px;
		margin-bottom: 20px;
		margin-left: auto;
		margin-right: auto;
		border-radius: 10px;
	}
	.post-editor-header-upper {
		display: flex;
		justify-content: space-between;
		align-items: center;
		width: 85%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 20px;
		padding: 10px;
	}
	.post-editor-header-upper-nav {
		font-size: x-large;
	}
	.post-editor-header-lower {
		display: flex;
		justify-content: flex-start;
		align-items: center;
		width: 85%;
		margin-left: auto;
		margin-right: auto;
		margin-bottom: 20px;
		padding: 10px;
		background-color: #f0f0f0;
		border-radius: 10px;
		box-shadow: 1px 1px 1px;
	}

	.post-editor-header-lower p {
		display: flex;
		width: 40px;
		text-align: center;
	}

	.post-editor-header-lower * {
		display: flex;
		align-items: center;
		/* margin-right: 5px; */
	}

	.post-editor-header-left {
		display: flex;
		justify-content: space-between;
	}

	.post-editor-header-category {
		flex-basis: 25%;
	}

	.post-editor-header-category-box {
	}

	.post-editor-header-title {
		flex-basis: 75%;
	}

	.post-editor-header-title-textfield {
	}

	.post-editor {
	}

	.post-editor-footer {
		display: flex;
		justify-content: flex-end;
		width: 90%;
		margin-left: auto;
		margin-right: auto;
		margin-top: 15px;
	}

	.post-editor-reactquill {
		background-color: #ffffff;
		padding-bottom: 40px;
		margin-left: auto;
		margin-right: auto;
		height: 400px;
		width: 90%;
	}
`;

// ▼ 링크를 상대경로에서 절대경로로
const Link = Quill.import('formats/link');
class MyLink extends Link {
	static create(value: string) {
		const node = super.create(value);
		value = this.sanitize(value);
		if (!value.startsWith('http')) {
			value = 'http://' + value;
		}
		node.setAttribute('href', value);
		return node;
	}
}
// ▲ 링크를 상대경로에서 절대경로로

const formats = [
	'header',
	'bold',
	'underline',
	'strike',
	'color',
	'align',
	'link',
	'image',
	'clean',
];

const modules = {
	toolbar: [
		[{ header: [false, 1, 2, 3, 4, 5] }],
		['bold', 'underline', 'strike'],
		[{ color: [] }],
		[{ align: [] }],
		['link', 'image'],
		['clean'],
	],
	clipboard: {
		matchVisual: false,
	},
};

// Quill.register('formats/my-link', MyLink);
// Quill.register('formats/align', Quill.import('attributors/style/align'));

const queryClient = new QueryClient();

const base_server_url = 'https://hypevoice.site';

const PostModifyComponent = () => {
	const navigation = useNavigate();
	const location = useLocation();
	const id = location.state.id;
	const getAccessToken = () => {
		const cookies = document.cookie.split('; ');
		const accessTokenCookie = cookies.find((cookie) =>
			cookie.startsWith('access_token='),
		);

		if (accessTokenCookie) {
			const accessToken = accessTokenCookie.split('=')[1];
			return accessToken;
		}
	};

	const getPost = async (): Promise<GetPostType> => {
		const response = await axios.get(base_server_url + `/api/boards/${id}`);
		return response.data;
	};

	const { data, isLoading, isFetched, isError } = useQuery<GetPostType>({
		queryKey: ['get-post'],
		queryFn: getPost,
		staleTime: 1000 * 60 * 5, // 5 minutes
	});

	const [title, setTitle] = useState<string>(data?.title || '');
	const [content, setContent] = useState<string>(data?.content || '');
	const [category, setCategory] = useState<string>(data?.category || '');

	const modifyPost = async (modifiedPost: CreatePostType): Promise<number> => {
		const token = getAccessToken();
		const response = await axios.patch(
			base_server_url + `/api/boards/${id}`,
			modifiedPost,
			{
				headers: {
					'Authorization': `Bearer ${token}`,
				},
			},
		);
		return response.data;
	};
	const { mutate } = useMutation({
		mutationFn: modifyPost,
		onError: () => {
			// console.log('modifyPost : On Error');
			alert('Error : 관리자에게 문의하세요');
		},
		onSuccess: () => {
			// console.log('modifyPost : Success');
			queryClient.invalidateQueries({ queryKey: ['post'] });
			alert('수정 성공!');
		},
		onSettled: () => {
			// console.log('modifyPost : On Settled');
		},
	});

	if (isLoading) {
		// console.log('Post : isLoading');
		return <LoadingComponent />;
	}

	if (isFetched) {
		// console.log('Post : isFetched');
		queryClient.invalidateQueries({ queryKey: ['get-post'] });
	}

	if (isError) {
		// console.log('Post : isError');
		return <div>게시물을 불러올 수 없습니다</div>;
	}

	const modifyPostHandler = (modifiedPost: CreatePostType) => {
		mutate(modifiedPost);
	};

	const onModify = (title: string, category: string, content: string) => {
		// console.log(content);
		const modifiedPost: CreatePostType = {
			category: category,
			title: title,
			content: content,
		};
		modifyPostHandler(modifiedPost);
	};
	// ▲ 임시로 데이터 저장하기 위한 것

	// useEffect(() => {
	// 	setCategory('');
	// 	setTitle('');
	// 	setContent('');
	// }, []);

	const reg = /<[^>]*>?/g;

	const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setTitle(event.target.value);
	};

	const handlePublish = () => {
		if (title.length < 5) {
			alert('제목의 내용이 너무 짧습니다');
			// console.log(content.replace(reg, ''));
			// console.log(content);
			// console.log(content.replace(reg, '').length);
			return;
		}

		if (content.replace(reg, '').length < 10) {
			alert('글의 내용이 너무 짧습니다');
			// console.log(content.replace(reg, ''));
			// console.log(content);
			// console.log(content.replace(reg, '').length);
			return;
		}

		// const processedContent = content.replace(
		// 	/<a /g,
		// 	'<a target="_blank" rel="noopener noreferrer" ',
		// );

		onModify(category, title, content);
		setCategory('');
		setTitle('');
		setContent('');
		// console.log('글을 수정합니다:', content);
		navigation(`/community/${id}`);
	};

	return (
		<PostEditorStyleDiv>
			<div className="post-editor-component">
				<div className="post-editor">
					<div className="post-editor-header">
						<div className="post-editor-header-upper">
							<div className="post-editor-header-upper-nav">
								<p>글 작성하기</p>
							</div>
							<Button
								variant="contained"
								className="post-tolist-button"
								onClick={() => {
									navigation('/community');
								}}
							>
								<p>목록</p>
							</Button>
						</div>
						<div className="post-editor-header-lower">
							<div className="post-editor-header-category">
								<div>분류 : {category} </div>
							</div>
							<div className="post-editor-header-title">
								<p>제목</p>
								<TextField
									className="post-editor-header-title-textfield"
									id="standard-basic"
									variant="standard"
									fullWidth
									value={title}
									onChange={handleTitleChange}
								/>
							</div>
						</div>
					</div>
					<ReactQuill
						className="post-editor-reactquill"
						theme="snow"
						modules={modules}
						formats={formats as unknown as string[]}
						value={content}
						onChange={setContent}
					/>
					<div className="post-editor-footer">
						<Button
							variant="contained"
							color="success"
							className="post-tolist-button"
							onClick={handlePublish}
							size="large"
						>
							<p>수정하기</p>
						</Button>
					</div>
				</div>
			</div>
		</PostEditorStyleDiv>
	);
};

export default PostModifyComponent;
