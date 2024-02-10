import { useQuery } from "@tanstack/react-query";
import { useRecoilValue } from "recoil";
import { WorkInfo } from "./type";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { axiosClient } from "@/api/axios";
import WorkTemplate from "./WorkTemplate";
import { useParams } from "react-router-dom";
import styled from "styled-components";
import { useEffect } from "react";

type WorkGridProps = {
  setWorkCount: React.Dispatch<React.SetStateAction<number>>;
};

const WorksGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 20px;
  padding: 1rem;
  margin: 1rem;
  @media (max-width: 850px) {
    grid-template-columns: repeat(2, 1fr);
  }
  @media (max-width: 550px), (max-height: 480px) {
    grid-template-columns: 1fr;
    grid-gap: 10px;
    grid-row-gap: 10px;
  }
  @media (max-height: 480px) {
    grid-gap: 5px;
    grid-row-gap: 5px;
  }
`;

export default function WorkGrid({ setWorkCount }: WorkGridProps) {
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const { voiceId } = useParams<{ voiceId: string }>();

  // const fetchWorks = async (): Promise<WorkInfo[]> => {
  //   try {
  //     const response = await axiosClient.get(`/api/voices/${voiceId}/works`);
  //     // 작업물이 하나도 없다면
  //     if (!response.data) {
  //       alert(
  //         "작업물이 아직 없습니다. 하입보이스에게 당신의 목소리를 들려주세요!"
  //       );
  //     } else {
  //       alert(
  //         `${response.data.length} 개의 목소리를 가진 ${currentMember.nickname}님 반갑습니다!`
  //       );
  //     }
  //     return response.data;
  //   } catch (error) {
  //     console.error(error);
  //     return [];
  //   }
  // };

  const mockFetchWorks = async (): Promise<WorkInfo[]> => {
    return new Promise((resolve) =>
      setTimeout(() => {
        resolve([
          {
            voiceId: 1,
            workId: 1,
            title: "작업물 제목",
            videoLink: "http://example.com/video1.mp4",
            photoUrl: "http://example.com/photo1.jpg",
            scriptUrl: "http://example.com/script1.txt",
            recordUrl: "http://example.com/record1.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 1,
              mediaClassification: "오디오드라마",
              voiceTone: "중음",
              voiceStyle: "밝은",
              gender: "남성",
              age: "청소년",
            },
          },
          {
            voiceId: 2,
            workId: 2,
            title: "작업물 제목",
            videoLink: "http://example.com/video2.mp4",
            photoUrl: "http://example.com/photo2.jpg",
            scriptUrl: "http://example.com/script2.txt",
            recordUrl: "http://example.com/record2.mp3",
            info: "작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. 작업물에 대한 설명입니다. ",
            isRep: 0,
            CategoryInfoValue: {
              workId: 2,
              mediaClassification: "외화",
              voiceTone: "저음",
              voiceStyle: "어두운",
              gender: "여성",
              age: "노년",
            },
          },
        ]);
      }, 1000)
    );
  };

  const { data: works } = useQuery({
    queryKey: ["works"],
    // queryFn: fetchWorks,
    queryFn: mockFetchWorks,
  });

  useEffect(() => {
    if (works) {
      setWorkCount(works.length);
    }
  }, [works]);

  return (
    <WorksGrid>
      {works ? (
        works.map((work) => <WorkTemplate key={work.workId} work={work} />)
      ) : (
        <p>
          아직 작업물이 없습니다. 하입보이스에서 당신의 목소리를 들려주세요!
        </p>
      )}
    </WorksGrid>
  );
}
