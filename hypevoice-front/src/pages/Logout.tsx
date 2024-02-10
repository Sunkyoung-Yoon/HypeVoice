import { useRecoilState } from "recoil";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { LoginState } from "@/recoil/Auth";
import { useNavigate } from "react-router-dom";
import { removeCookie } from "@/api/cookie";
import { useEffect } from "react";

export default function Logout() {
  const navigate = useNavigate();
  const [loginState, setLoginState] = useRecoilState(LoginState);
  const [currentMember, setCurrentMember] = useRecoilState(CurrentMemberAtom);

  useEffect(() => {
    // 로그인 상태를 false로 변경 // 로그인 상태 및 현재 유저 초기화
    setLoginState(false);
    setCurrentMember(null);

    // 쿠키에서 액세스 토큰을 제거
    removeCookie("access_token");

    // 사용자를 로그인 페이지로 리디렉션
    navigate("/");
  }, [setLoginState, navigate]);

  return <div>로그아웃 중...</div>;
}
