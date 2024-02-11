import { useQuery } from "@tanstack/react-query";
import { useRecoilValue } from "recoil";
import { WorkInfo } from "./type";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { axiosClient } from "@/api/axios";
import WorkTemplate from "./WorkTemplate";
import { useParams } from "react-router-dom";
import styled from "styled-components";
import { useEffect, useState } from "react";
import Pagination from "@mui/material/Pagination";

type WorkGridProps = {
  setWorkCount: React.Dispatch<React.SetStateAction<number>>;
};

// const WorksGrid = styled.div`
//   display: flex;
//   flex-wrap: wrap;
//   justify-content: flex-start;
//   padding: 15px;
//   margin: 15px;
// `;

const WorksGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 20px;
  padding: 15px;
  margin: 15px;
  overflow-x: auto;

  @media (max-width: 1000px) {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (max-width: 700px) {
    grid-template-columns: repeat(1, 1fr);
  }
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  padding-right: 15px;
`;

export default function WorkGrid({ setWorkCount }: WorkGridProps) {
  const [activePage, setActivePage] = useState(1);
  const worksPerPage = 12;
  const indexOfLastWork = activePage * worksPerPage;
  const indexOfFirstWork = indexOfLastWork - worksPerPage;
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
          {
            voiceId: 3,
            workId: 3,
            title: "작업물 제목 3",
            videoLink: "http://example.com/video3.mp4",
            photoUrl: "http://example.com/photo3.jpg",
            scriptUrl: "http://example.com/script3.txt",
            recordUrl: "http://example.com/record3.mp3",
            info: "3번째 작업물에 대한 설명입니다. 이 작업물은 애니메이션 분야의 작업물입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 3,
              mediaClassification: "애니메이션",
              voiceTone: "고음",
              voiceStyle: "부드러운",
              gender: "여성",
              age: "청소년",
            },
          },
          // 작업물 4
          {
            voiceId: 4,
            workId: 4,
            title: "작업물 제목 4",
            videoLink: "http://example.com/video4.mp4",
            photoUrl: "http://example.com/photo4.jpg",
            scriptUrl: "http://example.com/script4.txt",
            recordUrl: "http://example.com/record4.mp3",
            info: "4번째 작업물에 대한 설명입니다. 이 작업물은 광고 분야의 작업물입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 4,
              mediaClassification: "광고",
              voiceTone: "중음",
              voiceStyle: "담백한",
              gender: "남성",
              age: "청년",
            },
          },
          // 작업물 5
          {
            voiceId: 5,
            workId: 5,
            title: "작업물 제목 5",
            videoLink: "http://example.com/video5.mp4",
            photoUrl: "http://example.com/photo5.jpg",
            scriptUrl: "http://example.com/script5.txt",
            recordUrl: "http://example.com/record5.mp3",
            info: "5번째 작업물에 대한 설명입니다. 이 작업물은 게임 분야의 작업물입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 5,
              mediaClassification: "게임",
              voiceTone: "저음",
              voiceStyle: "친근한",
              gender: "남성",
              age: "성인",
            },
          },
          // 작업물 6
          {
            voiceId: 6,
            workId: 6,
            title: "작업물 제목 6",
            videoLink: "http://example.com/video6.mp4",
            photoUrl: "http://example.com/photo6.jpg",
            scriptUrl: "http://example.com/script6.txt",
            recordUrl: "http://example.com/record6.mp3",
            info: "6번째 작업물에 대한 설명입니다. 이 작업물은 오디오북 분야의 작업물입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 6,
              mediaClassification: "오디오북",
              voiceTone: "중음",
              voiceStyle: "따뜻한",
              gender: "여성",
              age: "청년",
            },
          },
          // 작업물 7
          {
            voiceId: 7,
            workId: 7,
            title: "작업물 제목 7",
            videoLink: "http://example.com/video7.mp4",
            photoUrl: "http://example.com/photo7.jpg",
            scriptUrl: "http://example.com/script7.txt",
            recordUrl: "http://example.com/record7.mp3",
            info: "7번째 작업물에 대한 설명입니다. 이 작업물은 뉴스 분야의 작업물입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 7,
              mediaClassification: "뉴스",
              voiceTone: "고음",
              voiceStyle: "명확한",
              gender: "여성",
              age: "성인",
            },
          },
          // 작업물 8
          {
            voiceId: 8,
            workId: 8,
            title: "작업물 제목 8",
            videoLink: "http://example.com/video8.mp4",
            photoUrl: "http://example.com/photo8.jpg",
            scriptUrl: "http://example.com/script8.txt",
            recordUrl: "http://example.com/record8.mp3",
            info: "8번째 작업물에 대한 설명입니다. 이 작업물은 교육 분야의 작업물입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 8,
              mediaClassification: "교육",
              voiceTone: "저음",
              voiceStyle: "신중한",
              gender: "남성",
              age: "노년",
            },
          },
          // 작업물 9
          {
            voiceId: 9,
            workId: 9,
            title: "작업물 제목 9",
            videoLink: "http://example.com/video9.mp4",
            photoUrl: "http://example.com/photo9.jpg",
            scriptUrl: "http://example.com/script9.txt",
            recordUrl: "http://example.com/record9.mp3",
            info: "9번째 작업물에 대한 설명입니다. 이 작업물은 낭독 분야의 작업물입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 9,
              mediaClassification: "낭독",
              voiceTone: "중음",
              voiceStyle: "감성적인",
              gender: "여성",
              age: "청소년",
            },
          },
          // 작업물 10
          {
            voiceId: 10,
            workId: 10,
            title: "작업물 제목 10",
            videoLink: "http://example.com/video10.mp4",
            photoUrl: "http://example.com/photo10.jpg",
            scriptUrl: "http://example.com/script10.txt",
            recordUrl: "http://example.com/record10.mp3",
            info: "10번째 작업물에 대한 설명입니다. 이 작업물은 라디오 분야의 작업물입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 10,
              mediaClassification: "라디오",
              voiceTone: "고음",
              voiceStyle: "사교적인",
              gender: "남성",
              age: "청년",
            },
          },
          // 작업물 11
          {
            voiceId: 11,
            workId: 11,
            title: "작업물 제목 11",
            videoLink: "http://example.com/video11.mp4",
            photoUrl: "http://example.com/photo11.jpg",
            scriptUrl: "http://example.com/script11.txt",
            recordUrl: "http://example.com/record11.mp3",
            info: "11번째 작업물에 대한 설명입니다. 이 작업물은 TV쇼 분야의 작업물입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 11,
              mediaClassification: "TV쇼",
              voiceTone: "중음",
              voiceStyle: "유쾌한",
              gender: "여",
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

  const currentWorks = works
    ? works.slice(indexOfFirstWork, indexOfLastWork)
    : [];

  const handlePageChange = (
    event: React.ChangeEvent<unknown>,
    value: number
  ) => {
    setActivePage(value);
  };

  return (
    <>
      <WorksGrid>
        {currentWorks ? (
          currentWorks.map((work) => (
            <WorkTemplate key={work.workId} work={work} />
          ))
        ) : (
          <p>
            아직 작업물이 없습니다. 하입보이스에서 당신의 목소리를 들려주세요!
          </p>
        )}
      </WorksGrid>
      <PaginationContainer>
        {" "}
        <Pagination
          count={Math.ceil(works ? works.length / worksPerPage : 0)}
          page={activePage}
          onChange={handlePageChange}
        />
      </PaginationContainer>
    </>
  );
}
