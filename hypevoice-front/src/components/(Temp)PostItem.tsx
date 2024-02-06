import React from 'react';
import { PostType } from './PostWriteComponent';

// 테스트용으로 임시로 만들어놓은거 (나중에 지울거임)
// 테스트용으로 임시로 만들어놓은거 (나중에 지울거임)
// 테스트용으로 임시로 만들어놓은거 (나중에 지울거임)

const PostItem: React.FC<PostType> = ({ id, content, created_date }) => {
	return (
		<div className="DiaryItem">
			<div className="info">
				<p>id : {id}</p>
				<span className="date">{new Date(created_date).toLocaleString()}</span>
			</div>
			<div
				className="content"
				dangerouslySetInnerHTML={{ __html: content }}
			></div>
		</div>
	);
};

export default PostItem;
