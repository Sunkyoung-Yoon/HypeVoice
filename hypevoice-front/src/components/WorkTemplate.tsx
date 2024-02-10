import React, { useState } from "react";
import { WorkInfo } from "./type";
import CustomAudioPlayer from "./CustomAudioPlayer";
import styled from "styled-components";

const WorkWrapper = styled.div`
  display: grid;
  grid-template-rows: auto 1fr auto;
  grid-gap: 1rem;
  margin-bottom: 1rem;
  width: 330px;
  height: 390px;
  aspect-ratio: 4/5;
  cursor: pointer;
  transition: opacity 0.3s;

  &:hover {
    opacity: 0.4;
  }

  @media (max-width: 480px) {
    margin-bottom: 0;
  }
`;

const ImageContainer = styled.div`
  flex: 3;
  display: flex;
  @media (max-width: 850px) {
    display: none;
  }
`;

const InfoWrapper = styled.div`
  box-sizing: border-box;
  border: 1px solid #000;
  border-radius: 10px;
  padding: 2px;
  width: 100%;
  height: 100px;
  overflow: auto;
  margin-top: 15px;

  @media (max-width: 850px) {
    display: none;
  }

  p {
    margin: 0.5rem;
    line-height: 1.2;
  }
`;

const TagContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  overflow: hidden;

  @media (max-width: 850px) {
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

function WorkTemplate({ work }: { work: WorkInfo }) {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleClick = () => {
    setIsModalOpen(true);
  };
  return (
    <WorkWrapper>
      <div
        style={{
          boxSizing: "border-box",
          border: "5px solid black",
          borderRadius: "25px",
          padding: "15px",
        }}
      >
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
          <CustomAudioPlayer src={work.recordUrl} />
        </div>
        <InfoWrapper>
          <p>{work.info}</p>
        </InfoWrapper>
      </div>
    </WorkWrapper>
  );
}

export default WorkTemplate;
