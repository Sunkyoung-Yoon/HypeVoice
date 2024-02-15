import { useQuery } from "@tanstack/react-query";
import { useRecoilValue } from "recoil";
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
    setSelectedWork(work);
  };

  // WorkModal 컴포넌트에서 모달을 닫음
  const handleClose = () => {
    setSelectedWork(null);
    setIsAddWorkModalOpen(false);
  };

  const fetchWorks = async (): Promise<WorkInfo[]> => {
    try {
      const response = await axiosClient.get(`/api/voices/${voiceId}/works`);
      // 작업물이 하나도 없다면
      if (!response.data || response.data.length === 0) {
        alert("아직 작업물이 없습니다.");
      }
      // else {
      //   alert(`${response.data.length} 개의 목소리를 찾았습니다.`);
      // }
      console.log(response.data);
      return response.data;
    } catch (error) {
      console.error(error);
      return [];
    }
  };

  const { data: works } = useQuery({
    queryKey: ["works"],
    queryFn: fetchWorks,
    // queryFn: mockFetchWorks,
  });

  useEffect(() => {
    if (works) {
      setWorkCount(works.length);
    }
    console.log(works);
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
        {currentWorks || currentWorks.length === 0 ? (
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
