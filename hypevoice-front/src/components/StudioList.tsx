// StudioList.tsx
import { useState } from "react";
import SearchIcon from "@mui/icons-material/Search";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { StudioInfo } from "./type";
import CreateStudioModal from "./CreateStudioModal";
import styled from "styled-components";
import InlineHeader from "./InlineHeader";
import { StudioListCurrentKeywordAtom } from "../recoil/CurrentKeyword/StudioListCurrentKeyword";
import Button from "@mui/material/Button";
import { useRecoilState } from "recoil";

// 방 목록 전체가 담기는 곳
const StudioListContainer = styled.div`
  min-height: 90vh;
  height: auto;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: stretch;
  background-color: white;
  justify-content: space-between;
`;

// 검색용
export const SearchBar = styled.div`
  display: flex;
  align-items: center;
  max-width: 500px;
  padding: 10px;
`;

// 검색어 입력란
export const SearchInput = styled.input`
  flex: 1;
  border: 1 solid blue;
  border-radius: 10px;
  margin-right: 5px;
  outline: none;
  padding: 10px;
`;

export const CurPageNum = styled.input`
  width: 30px;
  text-align: center;

  /* 추가한 CSS */
  &:focus {
    outline: none;
  }

  /* 작은 화살표 없애기 */
  -moz-appearance: textfield;
  &::-webkit-inner-spin-button,
  &::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  margin-right: 5px;
`;

// 이전/다음 버튼 스타일 수정
export const PrevNextButton = styled.button`
  background-color: #f5f5f5;
  border: none;
  padding: 8px 12px;
  margin-left: 10px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
`;

const StudiosContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  flex-grow: 1;
  flex: 1;
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 100%;
`;

const CreateStudioModalContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  width: 100%;
  flex: 1; // 추가
`;

const fetchStudios = async (): Promise<StudioInfo[]> => {
  const response = await axios.get<{ studioList: StudioInfo[] }>(
    "/api/studios"
  );
  return response.data.studioList;
};

export default function StudioList() {
  const [currentPage, setCurrentPage] = useState(1);
  // 검색어 관리
  const [searchText, setSearchText] = useRecoilState<string>(
    StudioListCurrentKeywordAtom
  );

  // 검색 버튼 누를 시 해당 검색어로 방 검색
  // 방 제목으로만 검색 가능!
  const handleSearch = () => {
    console.log(searchText + "로 검색한 결과");
  };

  // 방 리스트 React-Query로 불러와서 관리
  const {
    data: studioList,
    isLoading,
    isError,
  } = useQuery<StudioInfo[]>({
    queryKey: ["studios"],
    queryFn: fetchStudios,
    staleTime: 1000 * 60 * 5, // 5 minutes
  });
  const totalPages = studioList ? Math.ceil(studioList.length / 6) || 1 : 1;
  // const totalPages = studioList ? Math.ceil(studioList.length / 6) : 0;

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

  const handlePageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputPage = parseInt(e.target.value);
    if (inputPage >= 1 && inputPage <= totalPages) {
      setCurrentPage(inputPage);
    }
  };

  return (
    <StudioListContainer>
      <InlineHeader title={"방 목록"} worksCnt={0} storageSpace={0} />
      <SearchBar>
        <SearchInput
          maxLength={20}
          placeholder="방 제목으로 검색하세요. (최대 20자)"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <Button variant="text" onClick={handleSearch}>
          <SearchIcon />
        </Button>
      </SearchBar>
      {isLoading ? (
        <div style={{ marginLeft: "10px" }}>
          방 정보들을 가져오는 중입니다. 잠시만 기다려주세요.
        </div>
      ) : isError ? (
        <div style={{ marginLeft: "10px" }}>방 정보를 불러올 수 없습니다.</div>
      ) : (
        /*
        [스튜디오 리스트 == 방 목록]
        방 목록이 null 이 아니면 방 목록이 하나라도 있는 거
        slice() 메서드는 어떤 배열의 begin 부터 end 까지(end 미포함)에 대한 얕은 복사본을 새로운 배열 객체로 반환(원본 배열 안 바뀜!)

        */
        <StudiosContainer>
          {studioList &&
            studioList
              .slice((currentPage - 1) * 6, currentPage * 6)
              .map((studio) => (
                <div key={studio.sessionId}>
                  <h2>{studio.title}</h2>
                  <p>{studio.intro}</p>
                  <p>멤버 수: {studio.memberCount}</p>
                  <p>제한 인원: {studio.limitNumber}</p>
                  <p>{studio.isPublic ? "공개" : "비공개"} 방</p>
                </div>
              ))}
        </StudiosContainer>
      )}
      <PaginationContainer>
        <CurPageNum
          type="number"
          min="1"
          max={totalPages}
          value={currentPage}
          onChange={handlePageChange}
        />
        <p style={{ marginTop: "3px" }}>/ {totalPages}</p>
        <PrevNextButton onClick={handlePrevPage}>이전</PrevNextButton>
        <PrevNextButton onClick={handleNextPage}>다음</PrevNextButton>
      </PaginationContainer>
      <CreateStudioModalContainer>
        <CreateStudioModal />
      </CreateStudioModalContainer>
    </StudioListContainer>
  );
}
