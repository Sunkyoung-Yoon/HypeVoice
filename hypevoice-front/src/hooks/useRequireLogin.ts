import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { LoginState, CurrentMemberAtom } from "../recoil/Auth";

// 로그인 상태를 체크하는 커스텀 훅
export default function useRequireLogin() {
  const navigate = useNavigate();
  const isLoggedIn: boolean = useRecoilValue(LoginState); // 로그인 상태
  const { nickname } = useRecoilValue(CurrentMemberAtom); // CurrentMemberAtom에서 nickname 추출

  // 이 함수는 클릭 이벤트 핸들러에서 사용됩니다. // 클릭 시 가야할 곳의 위치를 string으로 넣어서 활용!
  return function (togo: string) {
    if (!isLoggedIn) {
      // 로그인이 되어있지 않다면
      if (
        window.confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?")
      ) {
        // 알림 메시지 표시 후
        // 확인 시
        navigate("/login"); // 로그인 페이지로 리다이렉트
      } // 취소 시 전 페이지로 이동
      else {
        navigate(-1);
      }
    }
    // 로그인이 되어 있을 경우
    else {
      if (togo) {
        if (togo === "/MyPage") {
          alert(`${nickname}님의 마이페이지로 이동합니다.`); // 알림 메시지 표시
          // navigate("/myPage");
          // } else {
          navigate(togo);
        } else navigate(togo);
      }
    }
  };
}
