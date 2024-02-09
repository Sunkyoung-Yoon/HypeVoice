// StudioList.tsx
import { useState } from "react";
import SearchIcon from "@mui/icons-material/Search";
import { useQuery } from "@tanstack/react-query";
import { StudioInfo } from "./type";
import CreateStudioModal from "./CreateStudioModal";
import styled from "styled-components";
import InlineHeader from "./InlineHeader";
import { StudioListCurrentKeywordAtom } from "../recoil/CurrentKeyword/StudioListCurrentKeyword";
import Button from "@mui/material/Button";
import { useRecoilState } from "recoil";
import { Typography } from "@mui/material";
import LockIcon from "@mui/icons-material/Lock";
import GroupIcon from "@mui/icons-material/Group";

const StudioListContainer = styled.div`
  min-height: 90vh;
  height: auto;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: white;
`;

// 스튜디오 한 개 전체 감싸는 div
const OuterDiv = styled.div`
  display: flex;
  flex-direction: row;
  border: 2px solid black;
  border-radius: 25px;
  padding: 20px;
  margin: 10px;
  width: 400px;
  height: 120px;

  @media (max-width: 600px) {
    width: calc((100% - 30px) / 2);
  }
`;

// LockWrapper 스타일 추가
const LockWrapper = styled.div`
  height: 24px; // 이 값은 필요에 따라 조정하시면 됩니다.
  display: flex;
  align-items: center;
  justify-content: center;
`;

const SearchAndPaginationContainer = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  padding: 10px;
`;

// 검색용
export const SearchBar = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  max-width: 700px;
  padding-inline: 10px;
`;

// 검색어 입력란
export const SearchInput = styled.input`
  flex: 2;
  border: 1px solid black transparent;
  border-radius: 10px;
  margin-right: 5px;
  outline: none;
  padding: 10px;
`;

// 현재 페이지 입력란
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
  white-space: nowrap;
  cursor: pointer;
`;

const StudiosContainer = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: space-arounds;
  align-items: flex-start;
  width: 100%;
`;

const StudioIntroDiv = styled.div`
  border: "1px solid black";
  overflow: "hidden";
  text-overflow: "ellipsis";
  white-space: "nowrap";
  height: "80px";

  @media (max-width: 600px) {
    display: none;
  }
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  height: 33px;
  width: 100%;
  margin-right: 20px;
`;

const CreateStudioModalContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 100%;
  padding: 10px;
`;

// JoinButton Style

const JoinButton = styled(Button)<{ canJoin: boolean }>`
  border: transparent !important;
  border-radius: 55px !important;
  padding-inline: 10px !important;
  white-space: nowrap;
  min-width: 100px;
  color: ${(props) =>
    props.canJoin ? "white !important" : "black !important"};
  background-color: ${(props) =>
    props.canJoin ? "#268aff !important" : "gray !important"};
  align-self: flex-end;
`;

const fetchStudios = async (): Promise<StudioInfo[]> => {
  // const response = await axios.get<{ studioList: StudioInfo[] }>(
  //   "/api/studios"
  // );
  const mockData: StudioInfo[] = Array.from({ length: 50 }, (_, i) => {
    // studioId는 총 6자리 숫자
    const studioId = String(i + 1).padStart(6, "0");

    // limitNumber는 최소1 최대 6
    const limitNumber = Math.floor(Math.random() * 6) + 1;

    // memberCount는 limitNumber보다 작거나 같은 자연수
    const memberCount = Math.floor(Math.random() * limitNumber) + 1;

    // title은 최대 20자의 한글
    const title = `연습 공간 ${studioId}`.slice(0, 20);

    // intro는 최대 100자의 한글
    const intro =
      `이 공간은 성우들이 연습하고 소통하는 곳입니다. 방 번호는 ${studioId}입니다.`.slice(
        0,
        100
      );

    return {
      studioId,
      sessionId: `ss${studioId}`,
      title,
      intro,
      memberCount,
      limitNumber,
      isPublic: Boolean(i % 2),
      password: Math.floor(Math.random() * 9000) + 1000,
      onair: Boolean(i % 2),
    };
  });

  return mockData;
  // return response.data.studioList;
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

  // 전체 페이지 수
  const totalPages = studioList ? Math.ceil(studioList.length / 6) || 1 : 1;

  // 이전 페이지로
  const handlePrevPage = () => {
    if (currentPage > 1) {
      setCurrentPage((prev) => prev - 1);
    }
  };

  // 다음 페이지로
  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage((prev) => prev + 1);
    }
  };

  // 페이지 수를 입력하면 바로 그 페이지로 이동
  const handlePageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const inputPage = parseInt(e.target.value);
    if (inputPage >= 1 && inputPage <= totalPages) {
      setCurrentPage(inputPage);
    }
  };

  return (
    <StudioListContainer>
      <div style={{ alignSelf: "flex-start", width: "65%" }}>
        <InlineHeader title={"방 목록"} worksCnt={0} storageSpace={0} />
      </div>
      <SearchAndPaginationContainer>
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
        <PaginationContainer>
          <CurPageNum
            type="number"
            min="1"
            max={totalPages}
            value={currentPage}
            onChange={handlePageChange}
          />
          <p style={{ marginTop: "3px", overflow: "hidden" }}>/ {totalPages}</p>
          <PrevNextButton onClick={handlePrevPage}>이전</PrevNextButton>
          <PrevNextButton onClick={handleNextPage}>다음</PrevNextButton>
        </PaginationContainer>
      </SearchAndPaginationContainer>

      {/* ----------------------------------------------------------------------------------------- */}
      {/* ----------------------------------------------------------------------------------------- */}
      {/* ----------------------------------------------------------------------------------------- */}

      {isLoading ? (
        <div style={{ marginLeft: "10px" }}>
          방 정보들을 가져오는 중입니다. 잠시만 기다려주세요.
        </div>
      ) : isError ? (
        <div style={{ marginLeft: "10px" }}>방 정보를 불러올 수 없습니다.</div>
      ) : (
        <StudiosContainer>
          {studioList &&
            studioList
              .slice((currentPage - 1) * 6, currentPage * 6) // 0번째부터 5(6-1)번째 // 이런 식으로 한 화면에 6개씩!
              .map((studio) => (
                <OuterDiv>
                  <div
                    className="left"
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      flexGrow: 5,
                      marginRight: "15px",
                    }}
                  >
                    <div
                      className="studioIdAndTitle"
                      style={{
                        display: "table",
                        tableLayout: "fixed",
                        width: "100%",
                      }}
                    >
                      <p
                        style={{
                          display: "table-cell",
                          width: "25%",
                          textOverflow: "ellipsis",
                          whiteSpace: "nowrap",
                          overflow: "hidden",
                        }}
                      >
                        {studio.studioId}
                      </p>
                      <h2
                        style={{
                          display: "table-cell",
                          width: "75%",
                          textOverflow: "ellipsis",
                          whiteSpace: "nowrap",
                          overflow: "hidden",
                        }}
                      >
                        {studio.title}
                      </h2>
                    </div>
                    <StudioIntroDiv>{studio.intro}</StudioIntroDiv>
                  </div>

                  <div
                    className="right"
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "space-around",
                    }}
                  >
                    <LockWrapper>
                      {studio.isPublic ? "" : <LockIcon />}
                    </LockWrapper>
                    <div
                      style={{
                        display: "flex",
                        flexDirection: "row",
                        justifyContent: "space-between",
                      }}
                    >
                      <GroupIcon />
                      <Typography>
                        {studio.memberCount} / {studio.limitNumber}
                      </Typography>
                    </div>
                    <JoinButton
                      canJoin={studio.memberCount < studio.limitNumber}
                    >
                      같이하기
                    </JoinButton>
                  </div>
                </OuterDiv>
              ))}
        </StudiosContainer>
      )}

      <CreateStudioModalContainer>
        <CreateStudioModal />
      </CreateStudioModalContainer>
    </StudioListContainer>
  );
}
