export interface GetPostType {
	boardId: number;
	title: string;
	view: number;
	content: string;
	category: string;
	createdDate: string;
	recordUrl: string;
	writerId: number;
	writerNickname: string;
}

export interface GetPageInfo {
	hasNext: boolean;
	numberOfElements: number;
	totalElements: number;
	totalPages: number;
}

export type CreatePostType = {
	title: string;
	content: string;
	category: string;
};

export type GetCommentType = {
	commentId: number;
	writerId: number;
	writerNickname: string;
	content: string;
	createdDate: string;
	voiceCommentUrl: string;
};

export type CreateCommentType = {
	content: string;
};

export type PostEditorProps = {
	initialTitleValue?: string;
	initialContentValue?: string;
	onCreate: (title: string, content: string) => void;
};

export interface CommentInputProps {
	onSubmit: (comment: string) => void;
	nickname: string; // 닉네임을 전달받는 prop 추가
}
