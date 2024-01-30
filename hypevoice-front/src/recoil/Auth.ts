import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

// recoilPersist 함수를 호출하고 그 결과로 반환되는 객체에서
// persistAtome 이라는 함수를 추출
// 프로퍼티가 하나 밖에 없지만서도 일종의 구조 분해 할당
const { persistAtom } = recoilPersist({
  key: "recoil-persist",
  storage: localStorage,
  converter: JSON,
});

// 현재 사용중인 유저
export const CurrentMemberAtom = atom({
  key: "CurrentMemberAtom",
  default: {
    memberid: 0,
    email: "",
    nickname: "",
    accessToken: "",
  },
  effects_UNSTABLE: [persistAtom],
});

// 로그인 여부
export const LoginState = atom({
  key: "LoginState",
  default: true,
  effects_UNSTABLE: [persistAtom],
});
