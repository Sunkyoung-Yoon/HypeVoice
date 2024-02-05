import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { LoginState } from "../recoil/Auth";

export default function MyInfo() {
  const navigate = useNavigate();
  const isLoggedIn = useRecoilValue(LoginState); // 로그인 상태
  const [isConfirmShown, setIsConfirmShown] = useState(false);

  useEffect(() => {
    if (!isLoggedIn && !isConfirmShown) {
      setIsConfirmShown(true);
      if (
        window.confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?")
      ) {
        navigate("/login");
      } else navigate("/");
    } else {
      setIsConfirmShown(false);
    }
  }, [isLoggedIn]);

  return (
    <>
      <h1>마이 페이지</h1>
    </>
  );
}
