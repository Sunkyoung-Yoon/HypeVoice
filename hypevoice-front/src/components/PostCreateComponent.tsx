import { Button, TextField } from '@mui/material';
import React, { useState } from 'react';
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import axios from 'axios';
import { CreatePostType } from './CommunityType';
import { useRecoilValue } from 'recoil';
import { LoginState } from '@/recoil/Auth';
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
		flex-basis: 95%;
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
	'video',
	'clean',
];

const modules = {
	toolbar: [
		[{ header: [false, 1, 2, 3, 4, 5] }],
		['bold', 'underline', 'strike'],
		[{ color: [] }],
		[{ align: [] }],
		['link', 'video'],
		['clean'],
	],
	clipboard: {
		matchVisual: false,
	},
};

Quill.register('formats/my-link', MyLink);
Quill.register('formats/align', Quill.import('attributors/style/align'));

const base_server_url = 'http://localhost:8081';
let id: number;
const PostCreateComponent = () => {
	const navigation = useNavigate();
	const queryClient = useQueryClient();
	const [title, setTitle] = useState<string>('');
	const [content, setContent] = useState<string>('');
	const [category, setCategory] = useState<string>('피드백');
	const isLogin = useRecoilValue(LoginState);
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

	// const createPost = async (newPost: CreatePostType): Promise<number> => {
	// 	console.log(`Bearer ${getAccessToken()}`);
	// 	const token = getAccessToken();
	// 	const response = await axios.post(
	// 		base_server_url + `/api/boards`,
	// 		newPost,
	// 		{
	// 			headers: {
	// 				'Authorization': `Bearer ${token}`,
	// 			},
	// 		},
	// 	);
	// 	return response.data;
	// };

	const createPost = async (newPost: CreatePostType): Promise<number> => {
		const token = getAccessToken();
		console.log(token);
		const data = new FormData();
		data.append(
			'request',
			new Blob([JSON.stringify(newPost)], { type: 'application/json' }),
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
				base_server_url + '/api/boards',
				data,
				{ headers },
			);
			id = response.data;
			return response.data;
		} catch (error) {
			console.error(error);
			return -1;
		}
	};

	const { data, mutate } = useMutation({
		mutationFn: createPost,

		onError: () => {
			console.log('createPost : On Error');
		},
		onSuccess: () => {
			console.log('createPost : Success');
			queryClient.invalidateQueries();
			console.log(data);
			navigation(`/community/${id}`);
		},
		onSettled: () => {
			queryClient.invalidateQueries();
			console.log('createPost : On Settled');
		},
	});

	const createPostHandler = (newPost: CreatePostType) => {
		console.log(newPost);
		mutate(newPost);
	};

	const onCreate = (title: string, content: string, category: string) => {
		const newPost: CreatePostType = {
			title: title,
			content: content,
			category: 'feedback',
		};
		createPostHandler(newPost);
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

	const handleContentChange = (value: string) => {
		setContent(value);
	};

	// const handleCategoryChange = (
	// 	event: React.ChangeEvent<{ value: unknown }>,
	// ) => {
	// 	setCategory(event.target.value as string);
	// };

	const handlePublish = () => {
		if (title.length < 5) {
			alert('제목의 내용이 너무 짧습니다');
			console.log(content.replace(reg, ''));
			console.log(content);
			console.log(content.replace(reg, '').length);
			return;
		}

		if (content.replace(reg, '').length < 10) {
			alert('글의 내용이 너무 짧습니다');
			console.log(content.replace(reg, ''));
			console.log(content);
			console.log(content.replace(reg, '').length);
			return;
		}

		// const processedContent = content.replace(
		// 	/<a /g,
		// 	'<a target="_blank" rel="noopener noreferrer" ',
		// );

		onCreate(title, content, category);
	};

	return (
		<PostEditorStyleDiv>
			{isLogin ? (
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
								{/* <div className="post-editor-header-category">
									<p>분류</p>
									<FormControl>
										<NativeSelect
											className="post-editor-header-category-box"
											value={category}
											onChange={handleCategoryChange}
											inputProps={{
												name: 'category',
												id: 'uncontrolled-native',
											}}
											id="demo-select-small"
										>
											<option value="자유">자유</option>
											<option value="피드백">피드백</option>
											<option value="구인구직">구인구직</option>
										</NativeSelect>
									</FormControl>
								</div> */}
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
							onChange={handleContentChange}
						/>
						<div className="post-editor-footer">
							<Button
								variant="contained"
								color="success"
								className="post-tolist-button"
								onClick={handlePublish}
								size="large"
							>
								<p>등록하기</p>
							</Button>
						</div>
						{/* <div>
							<AudioRecorder />
						</div> */}
					</div>
				</div>
			) : (
				<div>로그인이 필요합니다</div>
			)}
		</PostEditorStyleDiv>
	);
};

export default PostCreateComponent;
