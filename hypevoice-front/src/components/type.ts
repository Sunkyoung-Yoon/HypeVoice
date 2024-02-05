export type RequestHeader = {
  Authorization?: string;
  "Content-Type"?: string;
};

// 네이버 로그인 멤버
// url : http://localhost:8080/oauth2/authorization/naver
export type NaverUser = {
  memberId: number;
  nickname: string;
  role: string;
  accessToken: string;
  tokenResponse: {
    accessToken: string;
    refreshToken: string;
  };
};

// 카카오 로그인 멤버
// url : http://localhost:8080/oauth2/authorization/naver
export type KaKaoUser = {
  memberId: number;
  nickname: string;
  role: string;
  accessToken: string;
  tokenResponse: {
    accessToken: string;
    refreshToken: string;
  };
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
  sessionId: string;
  title: string;
  intro: string;
  memberCount: number;
  limitNumber: number;
  isPublic: boolean;
  password: string;
  onair: boolean;
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

// // 멤버 한명의 정보 수정
// // url : /api/members
// export type aaa = {};
