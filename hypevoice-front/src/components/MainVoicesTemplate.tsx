import { useRef, useState } from "react";
import { VoiceInfo } from "./type";
import styled from "styled-components";
import RepWork from "./RepWork";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { useSetRecoilState } from "recoil";
import { MyInfoVoiceId } from "@/recoil/CurrentVoiceId/MyInfoVoiceId";
import { useNavigate } from "react-router-dom";

interface WorkWrapperProps {
  isHovered: boolean;
}

const WorkWrapper = styled.div<WorkWrapperProps>`
  display: flex;
  flex-direction: column;
  border-radius: 25px;
  padding: 15px;
  margin: 15px;
  width: 330px;
  cursor: pointer;

  border: 1px solid ${(props) => (props.$isHovered ? "#5b5ff4" : "black")};
  transition: border-color 0.3s, border-width 0.3s;
`;

const StyledImg = styled.img`
  transition: opacity 0.3s ease;
  width: 100px;
  height: 100px;
  border-radius: 20%;
`;

const InfoContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end; /* Align items at the end of the cross axis */
`;

const NameSpan = styled.span`
  margin-left: 10px;
  font-size: 1.5em; /* Adjust this value to your preference */
`;

const LikesSpan = styled.span`
  font-size: 1.5em; /* Adjust this value to your preference */
`;

function MainVoicesTemplate({ voice }: { voice: VoiceInfo }) {
  const navigate = useNavigate();
  const templateRef = useRef(null);
  const [isHovered, setIsHovered] = useState(false);
  const setMyInfoVoiceId = useSetRecoilState(MyInfoVoiceId);

  const work = {
    voiceId: voice.voiceId,
    workId: -1, // voice 넘어올 때 workId는 없음
    title: voice.title,
    videoLink: "tmp", // voice 넘어올 때 videoLink는 없음
    photoUrl: voice.photoUrl,
    scriptUrl: "tmp", // voice 넘어올 때 scriptUrl은 없음
    recordUrl: voice.recordUrl,
    info: "tmp", // voice 넘어올 때 info는 없음
    isRep: 1, // voice 넘어올 때 무조건 대표 작업물만 넘어옴
    categoryInfoValue: {
      workId: -1, // voice 넘어올 때 workId는 없음
      mediaClassificationValue: voice.mediaClassificationValue,
      voiceToneValue: voice.voiceToneValue,
      voiceStyleValue: voice.voiceStyleValue,
      genderValue: voice.genderValue,
      ageValue: voice.ageValue,
    },
  };

  const handleClick = () => {
    setMyInfoVoiceId(voice.voiceId);
    navigate("/voice");
  };

  return (
    <WorkWrapper
      ref={templateRef}
      isHovered={isHovered}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={handleClick}
    >
      <RepWork work={work} />
      <InfoContainer>
        <div style={{ display: "flex", alignItems: "flex-end" }}>
          <StyledImg alt="profileImg" src={voice.imageUrl ? voice.imageUrl : "src/assets/basicImg.png"} />
          <NameSpan>{voice.name.length > 4 ? `${voice.name.slice(0, 4)}...` : voice.name}</NameSpan>
        </div>
        <div style={{ display: "flex", alignItems: "flex-end" }}>
          <FavoriteIcon
            color="error"
            fontSize="large"
            style={{ fontSize: 40 }}
          />{" "}
          {/* Adjust this value to your preference */}
          <LikesSpan>{voice.likes}</LikesSpan>
        </div>
      </InfoContainer>
    </WorkWrapper>
  );
}

export default MainVoicesTemplate;
