import { useQuery } from "@tanstack/react-query";
import { useRecoilState, useRecoilValue } from "recoil";
import { WorkInfo } from "./type";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { axiosClient } from "@/api/axios";
import WorkTemplate from "./WorkTemplate";
import styled from "styled-components";
import { useEffect, useState } from "react";
import Pagination from "@mui/material/Pagination";
import WorkModal from "./WorkModal";
import { MyInfoVoiceId } from "@/recoil/CurrentVoiceId/MyInfoVoiceId";

// 작업물 추가 버튼
const CreateWorkButton = styled.button`
  margin-left: 40px;
  width: 110px;
  border: none;
  border-radius: 25px;
  padding: 10px 15px;
  background-color: #5b5ff4;
  color: #fff;
  cursor: pointer;
`;

// 작업물들 갯수 넘겨주기 to Voice
type WorkGridProps = {
  setWorkCount: React.Dispatch<React.SetStateAction<number>>;
};

// 작업물들 들어가는 곳
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

// 페이지네이션용
const PaginationContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  padding-right: 15px;
`;

export default function WorkGrid({ setWorkCount }: WorkGridProps) {
  const [activePage, setActivePage] = useState(1); // 현재 페이지
  const worksPerPage = 12; // 한 페이지당 작업물 갯수
  const indexOfLastWork = activePage * worksPerPage;
  const indexOfFirstWork = indexOfLastWork - worksPerPage;
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const voiceId = useRecoilValue(MyInfoVoiceId);
  const [selectedWork, setSelectedWork] = useState<WorkInfo | null>(null);
  const [isAddWorkModalOpen, setIsAddWorkModalOpen] = useState(false);

  // 작업물 추가 버튼 클릭 => 모달 창 열리기
  const handleAddWorkClick = () => {
    setIsAddWorkModalOpen(true);
  };

  // 작업물 하나 클릭
  const handleWorkClick = (work: WorkInfo) => {
    alert(voiceId);
    alert(currentMember.memberId);
    setSelectedWork(work);
  };

  // WorkModal 컴포넌트에서 모달을 닫음
  const handleClose = () => {
    setSelectedWork(null);
    setIsAddWorkModalOpen(false);
  };

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
            title: "작업물 제목 1",
            videoLink: "http://example.com/video1.mp4",
            photoUrl: "http://example.com/photo1.jpg",
            scriptUrl: "http://example.com/script1.txt",
            recordUrl: "http://example.com/record1.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 1,
              mediaClassification: "오디오드라마",
              voiceTone: "저음",
              voiceStyle: "밝은",
              gender: "남성",
              age: "유아",
            },
          },
          {
            voiceId: 2,
            workId: 2,
            title: "작업물 제목 2",
            videoLink: "http://example.com/video2.mp4",
            photoUrl: "http://example.com/photo2.jpg",
            scriptUrl: "http://example.com/script2.txt",
            recordUrl: "http://example.com/record2.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 2,
              mediaClassification: "외화",
              voiceTone: "중음",
              voiceStyle: "따뜻한",
              gender: "여성",
              age: "아동",
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
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 3,
              mediaClassification: "게임",
              voiceTone: "고음",
              voiceStyle: "귀여운",
              gender: "기타",
              age: "청소년",
            },
          },
          {
            voiceId: 4,
            workId: 4,
            title: "작업물 제목 4",
            videoLink: "http://example.com/video4.mp4",
            photoUrl: "http://example.com/photo4.jpg",
            scriptUrl: "http://example.com/script4.txt",
            recordUrl: "http://example.com/record4.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 4,
              mediaClassification: "애니메이션",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "여성",
              age: "중장년",
            },
          },
          {
            voiceId: 5,
            workId: 5,
            title: "작업물 제목 5",
            videoLink: "http://example.com/video5.mp4",
            photoUrl: "http://example.com/photo5.jpg",
            scriptUrl: "http://example.com/script5.txt",
            recordUrl: "http://example.com/record5.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 5,
              mediaClassification: "내래이션",
              voiceTone: "고음",
              voiceStyle: "차가운",
              gender: "남성",
              age: "청소년",
            },
          },
          {
            voiceId: 6,
            workId: 6,
            title: "작업물 제목 6",
            videoLink: "http://example.com/video6.mp4",
            photoUrl: "http://example.com/photo6.jpg",
            scriptUrl: "http://example.com/script6.txt",
            recordUrl: "http://example.com/record6.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 6,
              mediaClassification: "기타",
              voiceTone: "중음",
              voiceStyle: "기타",
              gender: "여성",
              age: "노년",
            },
          },
          {
            voiceId: 7,
            workId: 7,
            title: "작업물 제목 7",
            videoLink: "http://example.com/video7.mp4",
            photoUrl: "http://example.com/photo7.jpg",
            scriptUrl: "http://example.com/script7.txt",
            recordUrl: "http://example.com/record7.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 7,
              mediaClassification: "외화",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "기타",
              age: "아동",
            },
          },
          {
            voiceId: 8,
            workId: 8,
            title: "작업물 제목 8",
            videoLink: "http://example.com/video8.mp4",
            photoUrl: "http://example.com/photo8.jpg",
            scriptUrl: "http://example.com/script8.txt",
            recordUrl: "http://example.com/record8.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 8,
              mediaClassification: "내래이션",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "여성",
              age: "유아",
            },
          },
          {
            voiceId: 9,
            workId: 9,
            title: "작업물 제목 9",
            videoLink: "http://example.com/video9.mp4",
            photoUrl: "http://example.com/photo9.jpg",
            scriptUrl: "http://example.com/script9.txt",
            recordUrl: "http://example.com/record9.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 9,
              mediaClassification: "기타",
              voiceTone: "고음",
              voiceStyle: "기타",
              gender: "여성",
              age: "청소년",
            },
          },
          {
            voiceId: 10,
            workId: 10,
            title: "작업물 제목 10",
            videoLink: "http://example.com/video10.mp4",
            photoUrl: "http://example.com/photo10.jpg",
            scriptUrl: "http://example.com/script10.txt",
            recordUrl: "http://example.com/record10.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 10,
              mediaClassification: "게임",
              voiceTone: "고음",
              voiceStyle: "차가운",
              gender: "여성",
              age: "아동",
            },
          },
          {
            voiceId: 11,
            workId: 11,
            title: "작업물 제목 11",
            videoLink: "http://example.com/video11.mp4",
            photoUrl: "http://example.com/photo11.jpg",
            scriptUrl: "http://example.com/script11.txt",
            recordUrl: "http://example.com/record11.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 11,
              mediaClassification: "외화",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "남성",
              age: "청소년",
            },
          },
          {
            voiceId: 12,
            workId: 12,
            title: "작업물 제목 12",
            videoLink: "http://example.com/video12.mp4",
            photoUrl: "http://example.com/photo12.jpg",
            scriptUrl: "http://example.com/script12.txt",
            recordUrl: "http://example.com/record12.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 12,
              mediaClassification: "게임",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "여성",
              age: "청소년",
            },
          },
          {
            voiceId: 13,
            workId: 13,
            title: "작업물 제목 13",
            videoLink: "http://example.com/video13.mp4",
            photoUrl: "http://example.com/photo13.jpg",
            scriptUrl: "http://example.com/script13.txt",
            recordUrl: "http://example.com/record13.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 13,
              mediaClassification: "애니메이션",
              voiceTone: "중음",
              voiceStyle: "차가운",
              gender: "여성",
              age: "노년",
            },
          },
          {
            voiceId: 14,
            workId: 14,
            title: "작업물 제목 14",
            videoLink: "http://example.com/video14.mp4",
            photoUrl: "http://example.com/photo14.jpg",
            scriptUrl: "http://example.com/script14.txt",
            recordUrl: "http://example.com/record14.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 0,
            CategoryInfoValue: {
              workId: 14,
              mediaClassification: "오디오드라마",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "여성",
              age: "아동",
            },
          },
          {
            voiceId: 15,
            workId: 15,
            title: "작업물 제목 15",
            videoLink: "http://example.com/video15.mp4",
            photoUrl: "http://example.com/photo15.jpg",
            scriptUrl: "http://example.com/script15.txt",
            recordUrl: "http://example.com/record15.mp3",
            info: "작업물에 대한 설명입니다.",
            isRep: 1,
            CategoryInfoValue: {
              workId: 15,
              mediaClassification: "내래이션",
              voiceTone: "고음",
              voiceStyle: "어두운",
              gender: "여성",
              age: "아동",
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
      {voiceId === currentMember.memberId && (
        <CreateWorkButton onClick={handleAddWorkClick}>
          작업물 추가
        </CreateWorkButton>
      )}
      {isAddWorkModalOpen && (
        <WorkModal
          open={isAddWorkModalOpen}
          onClose={handleClose}
          role="create"
          voiceId={voiceId}
          workId={0}
        />
      )}
      {selectedWork && (
        <WorkModal
          open={!!selectedWork}
          onClose={handleClose}
          role={
            selectedWork.voiceId === currentMember.memberId ? "change" : "read"
          }
          voiceId={voiceId}
          workId={selectedWork.workId}
        />
      )}
      <WorksGrid>
        {currentWorks ? (
          currentWorks.map((work) => (
            <WorkTemplate
              key={work.workId}
              work={work}
              onWorkClick={handleWorkClick}
            />
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
