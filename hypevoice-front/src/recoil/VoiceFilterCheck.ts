import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";
// import { categories } from "../components/Category";

// recoilPersist 함수를 호출하고 그 결과로 반환되는 객체에서
// persistAtome 이라는 함수를 추출
// 프로퍼티가 하나 밖에 없지만서도 일종의 구조 분해 할당
// JSON 형태로 localStorage에 저장하겠다는 의미! => 새로고침시에도 안날라감
const { persistAtom } = recoilPersist({
  key: "recoil-persist",
  storage: localStorage,
  converter: JSON,
});

// Categories 타입 정의
export interface Categories {
  [key: string]: string[];
}

// InitialState 타입 정의
export interface InitialState {
  [key: string]: {
    [key: string]: boolean;
  };
}

const categories: Categories = {
  미디어: ["오디오드라마", "외화", "게임", "애니메이션", "내래이션", "기타"],
  목소리톤: ["저음", "중음", "고음", "기타"],
  목소리스타일: [
    "밝은",
    "따뜻한",
    "귀여운",
    "어두운",
    "차가운",
    "권위있는",
    "기타",
  ],
  성별: ["남성", "여성", "기타"],
  연령: ["유아", "아동", "청소년", "청년", "중장년", "노년"],
};

/*
Object.keys(categories) => ["미디어", "목소리톤", "목소리스타일", "성별", "연령"]

여기서 reduce 함수를 통해
각각 category 들에 대해서 다음 화살표 함수를 진행한 값을 
다시 category에 넣어서 categories에 재배정

categoires[category]에 대해서 reduce 함수를 통해
각각 item 들에 대해서 다음 화살표 함수를 진행한 값을
다시 item에 넣어서 category에 재배정

결국
*/

const defaultState = Object.keys(categories).reduce<InitialState>(
  (acc, category) => {
    acc[category] = categories[category].reduce<{ [key: string]: boolean }>(
      (acc, item) => {
        acc[item] = false;
        return acc;
      },
      {}
    );
    return acc;
  },
  {}
);

export const voiceFilterCheckAtom = atom({
  key: "voiceFilterCheckAtom",
  default: defaultState,
  // effects_UNSTABLE: [persistAtom],
});
