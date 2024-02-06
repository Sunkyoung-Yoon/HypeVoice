import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function NaverLogin() {
  const navigate = useNavigate();
  useEffect(() => {
    navigate("/");
  });

  return <></>;
}
