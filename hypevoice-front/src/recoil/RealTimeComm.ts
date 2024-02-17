import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";

const { persistAtom } = recoilPersist({
  key: "recoil-persist",
  storage: localStorage,
  converter: JSON,
});

export const RtcInfo = atom({
  key: "RtcInfo",
  default: {
    roomRole: true,
    studioId: -1,
    title: "",
    intro: "",
    limitNumber: 1,
    isPublic: 1,
    password: null,
  },
  effects_UNSTABLE: [persistAtom],
  // 이 부분 주석처리하면 localstorage에 유지 X => 새로고침해서 테스트 해볼 수 있음!!
});
