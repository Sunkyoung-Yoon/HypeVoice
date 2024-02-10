import { useEffect, useState } from "react";
import { styled } from "@mui/material/styles";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import ButtonBase from "@mui/material/ButtonBase";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import { LoginState } from "../recoil/Auth";
import useRequireLogin from "@/hooks/useRequireLogin";

const Img = styled("img")({
  margin: "auto",
  display: "block",
  maxWidth: "100%",
  maxHeight: "100%",
});

function MyInfo() {
  const [favoriteCnt, setFavoriteCnt] = useState(0);
  const navigate = useNavigate();
  const isLoggedIn = useRecoilValue(LoginState); // 로그인 상태
  // const [isConfirmShown, setIsConfirmShown] = useState(false);

  useEffect(() => {
    if (
      !isLoggedIn // && !isConfirmShown
    ) {
      // setIsConfirmShown(true);
      if (
        window.confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?")
      ) {
        navigate("/login");
      } else navigate("/");
    } else {
      //setIsConfirmShown(false);
    }
  }, [isLoggedIn]);

  return (
    <>
      <h1>마이 페이지</h1>
    </>
  );
}
