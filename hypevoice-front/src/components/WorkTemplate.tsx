import { useState, useRef } from "react";
import { WorkInfo } from "./type";
import styled from "styled-components";
import RepWork from "./RepWork";
import { MyInfoVoiceId } from "@/recoil/CurrentVoiceId/MyInfoVoiceId";
import { useRecoilValue } from "recoil";
import WorkModal from "./WorkModal";
import { CurrentMemberAtom } from "@/recoil/Auth";

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

  border: 5px solid ${(props) => (props.$isHovered ? "#5b5ff4" : "black")};
  transition: border-color 0.3s, border-width 0.3s;
`;

const InfoWrapper = styled.div`
  box-sizing: border-box;
  border: 1px solid #000;
  border-radius: 10px;
  padding: 1px;
  width: 100%;
  height: 90px;
  overflow: auto;

  @media (max-width: 480px) {
    display: none;
  }

  p {
    margin: 0.5rem;
    line-height: 1.2;
  }
`;

interface WorkTemplateProps {
  work: WorkInfo;
  onWorkClick: (work: WorkInfo) => void;
}
export default function WorkTemplate({ work, onWorkClick }: WorkTemplateProps) {
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const voiceId = useRecoilValue(MyInfoVoiceId);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const templateRef = useRef(null);
  const [isHovered, setIsHovered] = useState(false);
  const role = work.voiceId === currentMember.memberId ? "change" : "read";

  const handleClick = () => {
    onWorkClick(work);
  };

  const handleClose = () => {
    setIsModalOpen(false);
  };

  return (
    <WorkWrapper
      ref={templateRef}
      $isHovered={isHovered}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={handleClick}
    >
      <RepWork work={work} />
      <InfoWrapper>
        <p>{work.info}</p>
      </InfoWrapper>
      {isModalOpen && (
        <WorkModal
          open={isModalOpen}
          onClose={handleClose}
          role={role}
          voiceId={voiceId}
          workId={work.workId}
        />
      )}
    </WorkWrapper>
  );
}
