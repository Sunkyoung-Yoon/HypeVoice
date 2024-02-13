import { atom } from "recoil";
  
// 메인 화면에 입력한 검색어(닉네임)
export const MyInfoVoiceId = atom({
key: "MyInfoVoiceId",
default: 0,
});