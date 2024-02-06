import styled from "styled-components";
import { useNavigate } from "react-router-dom";

const LogoContainer = styled.div`
  display: inline-block;
  cursor: pointer;
  border-radius: 15%;
  transition: border 0.3s ease;

  &:hover {
    border: 5px solid #5b5ff4;
  }
`;

const LogoImg = styled.img`
  display: block;
`;

export default function LogoComponent() {
  const navigate = useNavigate();
  return (
    <LogoContainer onClick={() => navigate("/")}>
      <LogoImg src="/src/assets/HYPE_VOICE_IMG.png" alt="HypeVoice Logo" />
    </LogoContainer>
  );
}
