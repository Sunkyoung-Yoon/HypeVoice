import React from 'react';
import PostItem from './(Temp)PostItem';
import { PostType } from './PostWriteComponent';

// 테스트용으로 임시로 만들어놓은거 (나중에 지울거임)
// 테스트용으로 임시로 만들어놓은거 (나중에 지울거임)
// 테스트용으로 임시로 만들어놓은거 (나중에 지울거임)

interface PostListProps {
	postList: PostType[];
}

const PostItemList: React.FC<PostListProps> = ({ postList }) => {
	return (
		<div className="DiaryList">
			<h2>게시물 리스트</h2>
			<h4>{postList.length}개의 게시물이 있습니다.</h4>
			<div>
				{postList.map((item) => (
					<PostItem key={`postitem_${item.id}`} {...item} />
				))}
			</div>
		</div>
	);
};

PostItemList.defaultProps = {
	postList: [],
};

export default PostItemList;
