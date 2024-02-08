import React, { useState } from "react";
import { useRecoilState } from "recoil";
import styled from "styled-components";
import SearchIcon from "@mui/icons-material/Search";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import { Button } from "@mui/material";
import CategoryFilter, { SmallIcon, IconKey } from "./CategoryFilter";
import { MainCurrentFilterAtom } from "../recoil/CurrentFilter/MainCurrentFilter";
import { MainCurrentKeyword } from "../recoil/CurrentKeyword/MainCurrentKeyword";

// 검색용 박스 전체 (검색 + 카테고리 선택 + 선택한 카테고리 태그)
const VoiceFilteringComponent = styled.div`
  width: 90%;
  border: 1px solid black;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
`;

// 검색용
export const SearchBar = styled.div`
  display: flex;
  align-items: center;
  padding: 10px;
`;

// 검색어 입력란
export const SearchInput = styled.input`
  flex: 1;
  border: none;
  border-radius: 10px;
  margin-right: 5px;
  outline: none;
  padding: 10px;
`;

// 카테고리 선택창
const CategoryContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  border-top: 1px solid black;
  // border-bottom: 1px solid black;
`;

// 카테고리 버튼
const CategoryButton = styled.button`
  display: flex;
  align-items: center;
  border: 2px solid;
  background: none;
`;

// 카테고리 필터 공간
const CategoryFilterDiv = styled.div`
  display: flex;
  align-items: center;
  border: 2px solid;
  background: none;
`;

// 태그들 뜨는 곳
const TagContainer = styled.div`
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  padding: 15px;
`;

const Tag = styled.div`
  background-color: #5b5ff4;
  padding: 5px 10px;
  margin: 5px;
  border: 1px solid;
  border-radius: 10px;
  display: flex;
  align-items: center;
  color: white;

  img {
    margin-right: 5px; // 아이콘과 글자 사이의 공간 늘리기
  }
`;

export type InitialState = {
  [category: string]: {
    [option: string]: boolean;
  };
};

export default function SearchComponent() {
  // 고른 것들 전역으로 관리
  const [voiceFilterCheck, setVoiceFilterCheck] = useRecoilState(
    MainCurrentFilterAtom
  );
  // 필터링 된 결과 관리
  const [filteredResults, setFilteredResults] = useState([]);
  // 검색어 관리
  const [searchText, setSearchText] =
    useRecoilState<string>(MainCurrentKeyword);
  // 카테고리 필터 띄울지 말지 => 아래 방향키 클릭에 따라서 토글!
  const [showDropdown, setShowDropdown] = useState(false);

  // 검색 버튼 누를 시 해당 검색어로 보이스 검색
  const handleSearch = () => {
    console.log(searchText + "로 검색한 결과");
  };

  // 확인 버튼 눌렀을 때 전역으로 관리되는 필터링 상태를 가지고
  // 조회한 대표작업물들의 결과를 가지고 저장.
  const handleConfirm = (filterState) => {
    const results = filterState; // 예시로 filterState

    // 필터링된 결과를 상태에 저장합니다.
    // setFilteredResults(results);

    // 팝업을 닫습니다.
    if (showDropdown) {
      toggleDropdown();
    }
  };

  // 체크한 옵션 바꾸기
  const handleCheckChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const [category, value] = event.target.name.split("_");

    setVoiceFilterCheck((prevState) => ({
      ...prevState,
      [category as keyof InitialState]: {
        ...(prevState[category as IconKey] || {}),
        [value]: event.target.checked,
      },
    }));
  };

  // 카테고리 필터 토글
  const toggleDropdown = () => {
    setShowDropdown(!showDropdown);
  };

  return (
    <VoiceFilteringComponent>
      <SearchBar>
        <SearchInput
          maxLength={20}
          placeholder="닉네임으로 검색하세요. (최대 20자)"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <Button variant="text" onClick={handleSearch}>
          <SearchIcon />
        </Button>
      </SearchBar>
      <CategoryContainer>
        <CategoryButton onClick={toggleDropdown}>
          <span>카테고리</span>
          <ArrowDropDownIcon />
        </CategoryButton>
      </CategoryContainer>
      {/* 카테고리 필터 여는 상태면 카테고리 필터 표시 */}
      {showDropdown && (
        <CategoryFilterDiv>
          <CategoryFilter
            onCheckChange={handleCheckChange}
            onConfirm={handleConfirm}
          />
        </CategoryFilterDiv>
      )}
      <TagContainer>
        {Object.entries(voiceFilterCheck).map(([category, options], index) => {
          const selectedOptions = Object.entries(options).filter(
            ([, checked]) => checked
          );
          if (selectedOptions.length === 0) return null;

          const [firstOption] = selectedOptions[0];
          const remainingCount = selectedOptions.length - 1;

          return (
            <Tag key={index} style={{ color: "white" }}>
              <SmallIcon which={category as IconKey} />
              {firstOption}
              {remainingCount > 0 && ` 외 ${remainingCount}개`}
            </Tag>
          );
        })}
      </TagContainer>
      <Button onClick={handleConfirm}>확인</Button>
    </VoiceFilteringComponent>
  );
}
