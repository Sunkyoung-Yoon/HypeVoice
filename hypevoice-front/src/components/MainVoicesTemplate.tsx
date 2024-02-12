import { useState, useRef } from "react";
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

const WorkWrapper = styled.div.attrs<WorkWrapperProps>(
  () => ({})
)<WorkWrapperProps>`
  display: flex;
  flex-direction: column;
  border: 5px solid black;
  border-radius: 25px;
  padding: 15px;
  margin: 15px;
  width: 330px;
  height: 390px;
  cursor: pointer;

  opacity: ${(props) => (props.isHovered ? 0.4 : 1)};
  transition: opacity 0.3s;
`;

const NonTransparentDiv = styled.div`
  opacity: 1 !important;
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

  // imageUrl: string;
  // likes: number
  // name: string;

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
    CategoryInfoValue: {
      workId: -1, // voice 넘어올 때 workId는 없음
      mediaClassification: voice.mediaClassificationValue,
      voiceTone: voice.voiceToneValue,
      voiceStyle: voice.voiceStyleValue,
      gender: voice.genderValue,
      age: voice.ageValue,
    },
  }

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
      <RepWork
        work={work}
        isHovered={isHovered}
        isRep={false}
        handleStarClick={function (): void {
          throw new Error("Function not implemented.");
        }}
      />
      <NonTransparentDiv>
        <InfoContainer>
          <div style={{ display: 'flex', alignItems: 'flex-end' }}>
            <StyledImg alt="profileImg" src={voice.imageUrl} />
            <NameSpan>{voice.name}</NameSpan>
          </div>
          <div style={{ display: 'flex', alignItems: 'flex-end' }}>
            <FavoriteIcon color="error" fontSize="large" style={{ fontSize: 40 }} /> {/* Adjust this value to your preference */}
            <LikesSpan>{voice.likes}</LikesSpan>
          </div>
        </InfoContainer>
      </NonTransparentDiv>
    </WorkWrapper>
  );
}

export default MainVoicesTemplate;