import React, { useEffect } from "react";
import { useRecoilState } from "recoil";
import { jwtDecode } from "jwt-decode";
import { LoginState } from "@/recoil/Auth";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { useNavigate } from "react-router-dom";
import { getCookie, removeCookie } from "../api/cookie";
import { DecodedTokenPayload, MemberInfo } from "@/components/type";
import axios from "axios";

export async function fetchMemberInfo(
  accessToken: string
): Promise<MemberInfo> {
  const response = await axios.get("/api/members", {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
  console.log("The Member is " + response.data);

  return response.data;
}

export default function AfterLogin() {
  const navigate = useNavigate();
  const [loginState, setLoginState] = useRecoilState(LoginState);
  const [currentMember, setCurrentMember] = useRecoilState(CurrentMemberAtom);

  useEffect(() => {
    alert("로그인 되었습니다.");
    // 쿠키에서 토큰을 가져옴
    const accessToken = getCookie("access_token");
    const refreshToken = getCookie("refresh_token");

    if (accessToken) {
      // 토큰이 유효한지 검사하는 로직
      try {
        const decoded: DecodedTokenPayload = jwtDecode(accessToken);
        const currentTime = Date.now() / 1000;
        const memberRole = decoded.role;
        const memberId = decoded.id;
        alert(memberRole + memberId + "님 환영합니다!");

        if (decoded.exp > currentTime) {
          // 토큰이 유효하다면 로그인 상태를 true로 변경
          setLoginState(true);
          // 현재 유저 (CurrentMemberAtom) 값 설정
          fetchMemberInfo(accessToken)
            .then((memberInfo: MemberInfo) => {
              setCurrentMember({
                ...currentMember,
                ...memberInfo,
                accessToken: accessToken,
              });
            })
            .catch((e) => {
              console.log(e);
            });
          console.log(currentMember);
          navigate("/");
        } else {
          // 토큰이 만료되었다면 로그인 상태를 false로 변경
          setLoginState(false);
          alert("유효하지 않은 토큰입니다! 다시 로그인 해주세요!");
          // 쿠키에서 토큰을 제거
          removeCookie("access_token");
        }
      } catch (e) {
        console.log(e);
      }
    } else {
      // 토큰이 없다면 로그인 상태를 false로 변경
      alert("토큰이 없습니다!");
      setLoginState(false);
      if (window.confirm("다시 로그인하시겠습니까?")) {
        navigate("/login");
      } else {
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
