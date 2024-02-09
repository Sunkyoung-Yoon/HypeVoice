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
  studioId: string;
  sessionId: string;
  title: string;
  intro: string;
  memberCount: number;
  limitNumber: number;
  isPublic: boolean;
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

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};
