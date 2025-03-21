import { atom } from 'recoil';
import { recoilPersist } from 'recoil-persist';

// recoilPersist 함수를 호출하고 그 결과로 반환되는 객체에서
// persistAtome 이라는 함수를 추출
// 프로퍼티가 하나 밖에 없지만서도 일종의 구조 분해 할당
// JSON 형태로 localStorage에 저장하겠다는 의미! => 새로고침시에도 안날라감
const { persistAtom } = recoilPersist({
	key: 'recoil-persist',
	storage: localStorage,
	converter: JSON,
});

// 현재 사용중인 유저
export const CurrentMemberAtom = atom({
	key: 'CurrentMemberAtom',
	default: {
		memberId: 0,
		email: '',
		nickname: '',
		profileUrl: '',
		accessToken: '',
	},
	effects_UNSTABLE: [persistAtom],
	// 이 부분 주석처리하면 localstorage에 유지 X => 새로고침해서 테스트 해볼 수 있음!!
});

// 로그인 여부
export const LoginState = atom({
	key: 'LoginState',
	default: false,
	effects_UNSTABLE: [persistAtom],
	// 이 부분 주석처리하면 localstorage에 유지 X => 새로고침해서 테스트 해볼 수 있음!!
});

// 입장해있는 studioId
export const StudioId = atom({
	key: 'StudioId',
	default: '',
	effects_UNSTABLE: [persistAtom],
});

export const MyStudioInfo = atom({
	key: 'MyStudioInfo',
	default: {
		title: '',
		intro: '',
		limitNumber: 0,
		isPublic: 1,
		password: null,
	},
});
