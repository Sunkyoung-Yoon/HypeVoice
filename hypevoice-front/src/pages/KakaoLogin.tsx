import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function KaKaoLogin() {
  const navigate = useNavigate();
  useEffect(() => {
    navigate("/");
  });
  return <></>;
}
