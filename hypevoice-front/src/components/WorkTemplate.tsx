import React, { useState, useRef } from "react";
import { WorkInfo } from "./type";
import CustomAudioPlayer from "./CustomAudioPlayer";
import styled from "styled-components";
import RepWork from "./RepWork";

interface WorkWrapperProps {
  isHovered: boolean;
}

const WorkWrapper = styled.div.attrs<WorkWrapperProps>(
  () => ({})
)<WorkWrapperProps>`
  display: flex;
  flex-direction: column;
  border-radius: 25px;
  padding: 15px;
  margin: 15px;
  width: 330px;
  height: 390px;
  cursor: pointer;

  border: 5px solid ${(props) => (props.isHovered ? "#5b5ff4" : "black")};
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

function WorkTemplate({ work }: { work: WorkInfo }) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const templateRef = useRef(null);
  const [isHovered, setIsHovered] = useState(false);
  const handleClick = () => {
    setIsModalOpen(true);
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
      <InfoWrapper>
        <p>{work.info}</p>
      </InfoWrapper>
    </WorkWrapper>
  );
}

export default WorkTemplate;
