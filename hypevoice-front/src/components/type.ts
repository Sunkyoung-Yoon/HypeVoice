export type RequestHeader = {
  Authorization?: string;
  "Content-Type"?: string;
};

export type MemberInfo = {
  memberId: string;
  email: string;
  profileUrl?: string;
  nickname?: string;
};

// 방 하나의 정보를 가져옴
// url : /api/studio
export type StudioInfo = {
  studioId: number;
  sessionId: string;
  title: string;
  intro: string;
  memberCount: number;
  limitNumber: number;
  isPublic: number; // 0 또는 1
  password: number;
  onair: boolean;
};

// // 멤버 한명의 정보 수정
// // url : /api/members
export type DecodedTokenPayload = {
  exp: number; // 만료
  iat: number; // 발생
  id: string; // 아이디
  role: string; // 역할
};

// // 방 한 개 생성 요청
// // method : post
// // url : /api/members (RequestBody)
export type MakeStudioData = {
  title: string;
  intro: string;
  limitNumber: number;
  isPublic: number;
  password: number | null;
};

// // 생성된 방의 Id를 담는 타입
// // url : /api/members
// // method : post (ResponseBody)
export type StudioResponse = {
  studioId: number | null;
};

// // 방 참가에 필요한 정보
// // url :/api/studios/connections/public 또는 private
export type joinStudioInfo = {
  studioId: number;
  sessionId: string;
  password?: number | null;
};

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};
