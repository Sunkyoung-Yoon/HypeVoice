import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { LoginState } from "../recoil/Auth";

// 로그인 상태를 체크하고, 이미 로그인이 되어있다면 메인 페이지로 리다이렉트하는 커스텀 훅
export function useRedirectionWhenLoggedIn() {
  const navigate = useNavigate();
  const isLoggedIn: boolean = useRecoilValue(LoginState); // 로그인 상태

  useEffect(() => {
    if (isLoggedIn) {
      // 이미 로그인이 되어있다면
      alert("이미 로그인한 상태입니다."); // 알림 메시지 표시
      navigate("/"); // 메인 페이지로 리다이렉트
    }
  }, [isLoggedIn, navigate]);
}
