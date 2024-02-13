import { atom } from "recoil";
import { recoilPersist } from "recoil-persist";
import { categories } from "../../components/Category";

// recoilPersist 함수를 호출하고 그 결과로 반환되는 객체에서
// persistAtome 이라는 함수를 추출
// 프로퍼티가 하나 밖에 없지만서도 일종의 구조 분해 할당
// JSON 형태로 localStorage에 저장하겠다는 의미! => 새로고침시에도 안 날라감
const { persistAtom } = recoilPersist({
  key: "recoil-persist",
  storage: localStorage,
  converter: JSON,
});

// OptionState 옵션마다 체크되어 있는 지 아닌 지에 따라서 체크!
export interface OptionState {
  [key: string]: {
    [key: string]: boolean;
  };
}

const defaultState = Object.keys(categories).reduce<OptionState>(
  (newCategories, category) => {
    newCategories[category] = categories[category].reduce<{
      [key: string]: boolean;
    }>((newCategory, option) => {
      newCategory[option] = false;
      return newCategory;
    }, {});
    return newCategories;
  },
  {}
);

export const VoiceCurrentFilterAtom = atom({
  key: "VoiceCurrentFilter",
  default: defaultState,
  effects_UNSTABLE: [persistAtom],
});
