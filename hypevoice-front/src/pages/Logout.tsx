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
    const logout = async () => {
      // 서버에 로그아웃 요청을 보낸 후 응답을 받습니다.
      try {
        // 서버에 로그아웃 요청을 보내는 코드를 작성합니다.
        // ...

        // 로그아웃 요청이 성공한 경우에만 아래 코드를 실행합니다.
        setLoginState(false); // 로그인 상태를 false로 변경
        setCurrentMember(null); // 현재 유저 정보 초기화

        // 쿠키에서 엑세스 토큰을 제거합니다.
        removeCookie("access_token");

        // 사용자를 로그인 페이지로 리디렉션합니다.
        navigate("/login");
      } catch (error) {
        console.error("로그아웃 에러:", error);
      }
    };

    logout();
  }, [setLoginState, setCurrentMember, navigate]);

  return <div>로그아웃 중...</div>;
}
