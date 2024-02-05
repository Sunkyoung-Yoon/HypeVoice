// StudioList.tsx
import React, { useState } from "react";
import { Search } from "@mui/icons-material";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { StudioInfo } from "./type";
import CreateStudioModal from "./CreateStudioModal";
import styled from "styled-components";

// 스타일 컴포넌트를 정의합니다.
const StudioListContainer = styled.div`
  height: 90vh; // 높이를 90%로 설정합니다.
  position: relative; // 자식 요소의 위치를 상대적으로 설정합니다.
  display: flex;
  flex-direction: column;
  align-items: flex-start;
`;

const SearchInputContainer = styled.div`
  display: flex;
  align-items: center;
`;

const StudiosContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  flex-grow: 1;
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 100%;
`;

const fetchStudios = async (): Promise<StudioInfo[]> => {
  const response = await axios.get<{ studioList: StudioInfo[] }>(
    "/api/studios"
  );
  return response.data.studioList;
};

export default function StudioList() {
  const [currentPage, setCurrentPage] = useState(1);

  const {
    data: studioList,
    isLoading,
    isError,
  } = useQuery<StudioInfo[]>({
    queryKey: ["studios"],
    queryFn: fetchStudios,
    staleTime: 1000 * 60 * 5, // 5 minutes
  });

  const totalPages = studioList ? Math.ceil(studioList.length / 6) : 0;

  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage((prev) => prev - 1);
    }
  };

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage((prev) => prev + 1);
    }
  };

  return (
    <StudioListContainer>
      <header>인라인 헤더</header>
      <SearchInputContainer>
        <input type="search" />
        <Search />
      </SearchInputContainer>
      {isLoading ? (
        <div>방 정보들을 가져오는 중입니다. 잠시만 기다려주세요.</div>
      ) : isError ? (
        <div>방 정보를 불러올 수 없습니다.</div>
      ) : (
        <StudiosContainer>
          {studioList &&
            studioList
              .slice((currentPage - 1) * 6, currentPage * 6)
              .map((studio) => (
                <div key={studio.sessionId}>
                  <h2>{studio.title}</h2>
                  <p>{studio.intro}</p>
                </div>
              ))}
        </StudiosContainer>
      )}
      <PaginationContainer>
        <p>
          {currentPage} / {totalPages}
        </p>
        <button onClick={handlePrevPage}>이전</button>
        <button onClick={handleNextPage}>다음</button>
      </PaginationContainer>
      <CreateStudioModal />
    </StudioListContainer>
  );
}
