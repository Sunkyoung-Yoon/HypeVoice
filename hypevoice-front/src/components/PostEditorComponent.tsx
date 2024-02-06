import { Button, FormControl, NativeSelect, TextField } from '@mui/material';
import React, { useEffect, useState } from 'react';
import ReactQuill, { Quill } from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
// import AudioRecord from './AudioRecord';

export type PostEditorProps = {
	initialValue?: string;
	onCreate: (content: string) => void;
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
Quill.register('formats/align', Quill.import('attributors/style/align'));
Quill.register('formats/size', Quill.import('attributors/style/size'));
// ▲ 링크를 상대경로에서 절대경로로

const PostEditorComponent: React.FC<PostEditorProps> = ({
	initialValue = '',
	onCreate,
}) => {
	const navigation = useNavigate();
	const [content, setContent] = useState<string>(initialValue);

	useEffect(() => {
		setContent(initialValue);
	}, [initialValue]);
	const reg = /<[^>]*>?/g;

	const handleEditorChange = (value: string) => {
		setContent(value);
	};

	const handlePublish = () => {
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
		onCreate(processedContent);
		alert('저장 성공');
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
									// label="Standard"
									variant="standard"
									fullWidth
								/>
							</div>
						</div>
					</div>
					<ReactQuill
						className="post-editor-reactquill"
						theme="snow"
						value={content}
						modules={modules}
						formats={formats as unknown as string[]}
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

export default PostEditorComponent;
