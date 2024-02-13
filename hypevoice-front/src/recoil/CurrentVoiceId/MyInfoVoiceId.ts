import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

// JSON 형태로 localStorage에 저장하겠다는 의미! => 새로고침시에도 안날라감
const { persistAtom } = recoilPersist({
  key: "recoil-persist",
  storage: localStorage,
  converter: JSON,
});

// 메인 화면에 입력한 검색어(닉네임)
export const MyInfoVoiceId = atom({
  key: "MyInfoVoiceId",
  default: 0,
  effects_UNSTABLE: [persistAtom],
});
