import { VoiceDataType } from "@/components/type";
import { atom } from "recoil";

// VoiceData 상태를 관리하는 atom 생성
export const voiceDataAtom = atom<VoiceDataType>({
  key: "voiceData", // unique ID
  default: {
    // default initial value
    name: "",
    imageUrl: "",
    intro: "",
    email: "",
    phone: "",
    addInfo: "",
    likes: 0,
    totalSizeMega: 0,
  },
});
