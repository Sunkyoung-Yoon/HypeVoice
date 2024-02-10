import styled from "styled-components";
import { useNavigate } from "react-router-dom";

const LogoContainer = styled.div`
  display: flex;
  justify-content: center;
  align-self: center;
  cursor: pointer;
  transition: opacity 0.1s ease, border 0.1s ease;
  border-radius: 25%;
  transition: border 0.1s ease;
  padding: 20px;

  &:hover {
    opacity: 60%;
  }
  @media (max-height: 600px) and (max-width: 600px) {
    display: none;
  }
  @media (max-width: 300px) {
    display: none;
  }
  @media (max-height: 400px) {
    display: none;
  }
`;

const LogoImg = styled.img`
  display: block;
  width: 300px;
  height: 200px;
`;

export default function LogoComponent() {
  const navigate = useNavigate();
  return (
    <LogoContainer onClick={() => navigate("/")}>
      <LogoImg src="/src/assets/HYPE_VOICE_IMG.png" alt="HypeVoice Logo" />
    </LogoContainer>
  );
}