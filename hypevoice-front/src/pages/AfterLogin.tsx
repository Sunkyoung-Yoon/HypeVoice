import { useEffect } from "react";
import { useRecoilState, useRecoilValue, useSetRecoilState } from "recoil";
import { jwtDecode } from "jwt-decode";
import { LoginState } from "@/recoil/Auth";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { useNavigate } from "react-router-dom";
import { getCookie, setCookie, removeCookie } from "../api/cookie";
import { getCookie, setCookie, removeCookie } from "../api/cookie";
import { DecodedTokenPayload, MemberInfo } from "@/components/type";
import { axiosClient } from "@/api/axios";

const fetchMemberInfo = async (accessToken: string) => {
  const response = await axiosClient.get("/api/members", {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
  console.log(response.data); // response.data가 멤버info
  return response.data;
};
};

export default function AfterLogin() {
  const navigate = useNavigate();
  const [loginState, setLoginState] = useRecoilState(LoginState);
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const setCurrentMemberInfo = useSetRecoilState(CurrentMemberAtom);

  useEffect(() => {
    const accessToken = getCookie("access_token");
    const refreshToken = getCookie("refresh_token");

    console.log(accessToken);
    console.log(refreshToken);

    console.log(accessToken);
    console.log(refreshToken);

    if (accessToken) {
      // 토큰이 유효한지 검사하는 로직
      try {
        // 토큰의 페이로드
        // 토큰의 페이로드
        const decoded: DecodedTokenPayload = jwtDecode(accessToken);
        console.log("decoded = ");
        console.log(decoded);
        const currentTime = Date.now() / 1000;
        const memberRole = decoded.role;
        const memberId = decoded.id;
        alert(memberRole + memberId + "님 환영합니다!");

        if (decoded.exp > currentTime) {
          // 토큰이 유효하다면
          // 로그인 상태를 true로 변경
          setLoginState(true);
          // accessToken 새로 설정
          setCookie("access_token", accessToken);
          // 현재 유저 (CurrentMemberAtom) 값 설정
          fetchMemberInfo(accessToken)
            .then((memberInfo: MemberInfo) => {
              setCurrentMemberInfo({
                ...currentMember,
                ...memberInfo,
                // accessToken: accessToken, // 엑세스 토큰은 제외하고!
              });
              console.log("memberInfo is ");
              console.log(memberInfo);
              console.log("currentMember is ");
              console.log(currentMember);
            })
            .catch((e) => {
              console.log(e);
            });
          navigate("/");
        } else {
          // 토큰이 만료되었다면 로그인 상태를 false로 변경
          setLoginState(false);
          alert("유효하지 않은 토큰입니다! 다시 로그인 해주세요!");
          // 쿠키에서 토큰을 제거
          removeCookie("access_token");
          navigate("/");
        }
      } catch (error) {
        console.error("Error occurred while fetching member info:", error);
      } catch (error) {
        console.error("Error occurred while fetching member info:", error);
      }
    } else {
      // 토큰이 없다면
      alert("토큰이 없습니다!");
      // 로그인 상태를 false로 변경
      setLoginState(false);
      // 사용자가 다시 로그인 시도를 원할 경우 login 페이지로
      if (window.confirm("다시 로그인하시겠습니까?")) {
        navigate("/login");
      }
      // 아니면 홈으로!
      else {
        navigate("/");
      }
    }
  }, [setLoginState]);

  return (
    <div>
      <h1>로그인 후처리 과정 진행 중입니다.</h1>
    </div>
  );
}
