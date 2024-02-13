export type RequestHeader = {
  Authorization?: string;
  "Content-Type"?: string;
};

export type MemberInfo = {
  memberId: string;
  email?: string;
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

// // 멤버 한 명의 정보
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
export type JoinStudioInfo = {
  studioId: number;
  sessionId: string;
  password?: number | null;
};

// // 작업물 한 개의 정보
// // url :
export type WorkInfo = {
  voiceId: number; // 보이스 번호 => memberId랑 비교해서 수정 및 삭제 가능여부 판단
  workId: number; // 작업물 자체 번호
  title: string; // 작업물 제목 // 최대 20자
  videoLink: string; // 영상 링크
  photoUrl: string; // 사진 링크
  scriptUrl: string; // 대본 링크
  recordUrl: string; // 녹음 파일 링크
  info: string; // 소개 // 최대 100자
  isRep: number; // 대표 작업물 여부
  categoryInfoValue: {
    workId: number; // 작업물 자체 번호
    mediaClassificationValue: string; // 미디어
    voiceToneValue: string; // 톤
    voiceStyleValue: string; // 스타일
    genderValue: string; // 성별
    ageValue: string; // 나이
  };
};

// // 보이스 한 개의 정보
// // url :
export type VoiceInfo = {
  ageValue: string;
  genderValue: string;
  imageUrl: string;
  likes: number;
  mediaClassificationValue: string;
  name: string;
  photoUrl: string;
  recordUrl: string;
  title: string;
  voiceId: number;
  voiceStyleValue: string;
  voiceToneValue: string;
};

// // 카테고리 선택한 값
// // url : /api/members
export type CategoryInfoValue = {
  mediaClassification: string;
  voiceTone: string;
  voiceStyle: string;
  gender: string;
  age: string;
};

// // 내용
// // url :
export type ChangeIsRep = {
  voiceId: number;
  workId: number;
};

// 작업물 만들기 모달
// url :
export type WorkData = {
  title: string;
  videoLink: string;
  info: string;
  isRep: number;
  CategoryInfoRequest: {
    mediaClassification: string;
    voiceTone: string;
    voiceStyle: string;
    gender: string;
    age: string;
  };
};

// // BLOB 이미지
export type ImageUpload = {
  file: File;
  thumbnail: string;
  type: string;
};

// // 작업물 관련 모달창 props type
// // url :
export type WorkModalProps = {
  open: boolean;
  onClose: () => void;
  role: string;
  voiceId: number;
  workId: number;
};

export type SelectedCategory = {
  미디어: string;
  목소리톤: string;
  목소리스타일: string;
  성별: string;
  연령: string;
};
