import { Button, FormControl, NativeSelect, TextField } from '@mui/material';
import React, { useEffect, useRef, useState } from 'react';
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { QueryClient, useMutation } from '@tanstack/react-query';
import axios from 'axios';
// import AudioRecord from './AudioRecord';

export type PostEditorProps = {
	initialTitleValue?: string;
	initialContentValue?: string;
	onCreate: (title: string, content: string) => void;
};

export type CreatePostType = {
	title: string;
	content: string;
	category: string;
};

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

Quill.register('formats/my-link', MyLink);
// Quill.register('formats/align', Quill.import('attributors/style/align'));

const queryClient = new QueryClient();
const PostCreateComponent = () => {
	const navigation = useNavigate();
	const [title, setTitle] = useState<string>('');
	const [content, setContent] = useState<string>('');

	// ▼ 임시로 데이터 저장하기 위한 것
	const [tempData, setTempData] = useState<CreatePostType[]>([]);
	const tempDataId = useRef<number>(0);

	const createPost = async (newPost: CreatePostType) => {
		return await axios.post(`/api/boards`, newPost);
	};

	const { mutate } = useMutation({
		mutationFn: createPost,
		onError: () => {
			console.log('createPost : On Error');
		},
		onSuccess: () => {
			console.log('createPost : Success');
			queryClient.invalidateQueries({ queryKey: ['post'] });
			alert('등록 성공!');
		},
		onSettled: () => {
			console.log('createPost : On Settled');
		},
	});

	const createPostHandler = (newPost: CreatePostType) => {
		mutate(newPost);
	};

	const onCreate = (title: string, category: string, content: string) => {
		console.log(content);
		const newPost: CreatePostType = {
			title: title,
			category: category,
			content: content,
		};
		createPostHandler(newPost);
	};
	// ▲ 임시로 데이터 저장하기 위한 것

	useEffect(() => {
		setTitle('');
		setContent('');
	}, []);
	const reg = /<[^>]*>?/g;

	const handleTitleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
		setTitle(event.target.value);
	};

	const handleEditorChange = (value: string) => {
		setContent(value);
	};

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

		const processedContent = content.replace(
			/<a /g,
			'<a target="_blank" rel="noopener noreferrer" ',
		);

		onCreate(title, '자유', processedContent);
		setTitle('');
		setContent('');
		console.log('글을 게시판에 등록합니다:', content);
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
								<p>분류</p>
								<FormControl>
									<NativeSelect
										className="post-editor-header-category-box"
										defaultValue={'자유'}
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
						onChange={handleEditorChange}
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
				</div>
			</div>
		</PostEditorStyleDiv>
	);
};

export default PostCreateComponent;
