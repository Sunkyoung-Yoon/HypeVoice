import React, { useRef, useState } from 'react';
import PostEditorComponent from './PostEditorComponent';
import PostItemList from './(Temp)PostItemList';
import styled from 'styled-components';
import { useMutation } from '@tanstack/react-query';
import { TestPostType } from './CommunityComponent';
import axios from 'axios';
// import AudioRecord from './AudioRecord';

// ▼ 임시로 데이터 저장하기 위한 것
export type PostType = {
	id: number;
	content: string;
	created_date: number;
};
// ▲ 임시로 데이터 저장하기 위한 것

const PostWriteStyleDiv = styled.div`
	.post-write-container {
		width: 95%;
		margin-left: auto;
		margin-right: auto;
		border-radius: 10px;
	}
`;

const PostWriteComponent: React.FC = () => {
	const mutation = useMutation({
		mutationFn: (data: TestPostType) => {
			console.log(data);
			return axios.post('https://jsonplaceholder.typicode.com/posts', data);
		},
	});

	// ▼ 임시로 데이터 저장하기 위한 것
	const [tempData, setTempData] = useState<PostType[]>([]);
	const tempDataId = useRef<number>(0);

	const onCreate = (content: string) => {
		console.log(content);
		const created_date = new Date().getTime();
		const newTempData: PostType = {
			content,
			created_date,
			id: tempDataId.current,
		};
		tempDataId.current += 1;
		setTempData([newTempData, ...tempData]);
	};
	// ▲ 임시로 데이터 저장하기 위한 것

	return (
		<PostWriteStyleDiv>
			<div className="post-write-container">
				<div>
					{/* <div>
						<AudioRecord />
					</div> */}
					<PostEditorComponent onCreate={onCreate} />
					<PostItemList postList={tempData} />
				</div>
				<div>
					<div>
						<hr />
						{mutation.isPending ? (
							'Adding Data...'
						) : (
							<>
								{mutation.isError ? (
									<div>An error occurred: {mutation.error.message}</div>
								) : null}

								{mutation.isSuccess ? <div>Data added!</div> : null}

								<button
									onClick={() => {
										mutation.mutate({
											id: 999,
											userId: 888,
											title: '글제목글제목글제목글제목글제목',
											body: '글내용글내용글내용글내용글내용',
										});
									}}
								>
									Post 통신 확인
								</button>
							</>
						)}
					</div>
				</div>
			</div>
		</PostWriteStyleDiv>
	);
};

export default PostWriteComponent;
