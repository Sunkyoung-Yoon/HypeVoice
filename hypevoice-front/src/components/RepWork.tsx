import { WorkInfo } from "./type";
import CustomAudioPlayer from "./CustomAudioPlayer";
import styled from "styled-components";
import { useRecoilValue } from "recoil";
import { CurrentMemberAtom } from "@/recoil/Auth";

interface WorkWrapperProps {
  isHovered: boolean; // 마우스 올라갔는 지 여부
  isRep: boolean; // 대표 작업물 여부
}

const WorkWrapper = styled.div.attrs<WorkWrapperProps>(
  () => ({})
)<WorkWrapperProps>`
  display: flex;
  flex-direction: column;
  width: 330px;
  height: 390px;
  cursor: pointer;
  opacity: ${(props) => (props.isHovered ? 0.4 : 1)};
  transition: opacity 0.3s;
`;

interface StarProps {
  isRep: boolean;
}

const Star = styled.div<StarProps>`
  position: relative;
  top: 10px;
  left: 10px;
  cursor: pointer;
  color: ${(props) => (props.isRep ? "yellow" : "transparent")};
  border: 1px solid;
  border-color: ${(props) => (props.isRep ? "yellow" : "grey")};
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const NonTransparentDiv = styled.div`
  opacity: 1 !important;
`;

const ImageContainer = styled.div`
  flex: 3;
  display: flex;

  @media (max-width: 480px) {
    display: none;
  }
`;

const TagContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  overflow: hidden;

  @media (max-width: 480px) {
    display: none;
  }
`;

const Tag = styled.span`
  background-color: #5b5ff4;
  color: #fff;
  padding: 0.2rem 0.8rem;
  border-radius: 25px;
  display: inline-block;
  text-align: center;
  margin: 0.1rem;
  width: 70px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
`;

export default function RepWork({
  work,
  isHovered,
  isRep,
  handleStarClick,
}: {
  work: WorkInfo;
  isHovered: boolean;
  isRep: boolean;
  handleStarClick: () => void;
}) {
  const currentMember = useRecoilValue(CurrentMemberAtom);
  return (
    <WorkWrapper isHovered={isHovered} isRep={true}>
      {work.voiceId === currentMember.memberId && (
        <Star isRep={isRep} onClick={handleStarClick}>
          왜 아무것도 안보이니?
        </Star>
      )}
      <div>
        <div style={{ display: "flex", marginBottom: "15px" }}>
          <ImageContainer>
            <img
              src={work.photoUrl}
              alt={work.title}
              style={{ width: "100%", height: "100%", objectFit: "cover" }}
            />
          </ImageContainer>
          <div
            style={{
              flex: "1",
              display: "flex",
              flexDirection: "column",
              justifyContent: "space-around",
            }}
          >
            <TagContainer>
              <Tag>{work.CategoryInfoValue.mediaClassification}</Tag>
              <Tag>{work.CategoryInfoValue.voiceTone}</Tag>
              <Tag>{work.CategoryInfoValue.voiceStyle}</Tag>
              <Tag>{work.CategoryInfoValue.gender}</Tag>
              <Tag>{work.CategoryInfoValue.age}</Tag>
            </TagContainer>
          </div>
        </div>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            marginBottom: "15px",
          }}
        >
          <h2
            style={{
              overflow: "hidden",
              textOverflow: "ellipsis",
              marginBottom: "20px",
            }}
          >
            {work.title}
          </h2>
          <NonTransparentDiv>
            <CustomAudioPlayer src={work.recordUrl} />
          </NonTransparentDiv>
        </div>
      </div>
    </WorkWrapper>
  );
}
