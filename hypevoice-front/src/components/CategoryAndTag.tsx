// CategoryAndTag.tsx
import React, { useState } from "react";
import { RecoilState, useRecoilState } from "recoil";
import styled from "styled-components";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import CategoryFilter, { SmallIcon, IconKey } from "./CategoryFilter";
import {
  MainCurrentFilterAtom,
  defaultState,
} from "../recoil/CurrentFilter/MainCurrentFilter";

// 카테고리 선택창
const CategoryContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  /* border-top: 1px solid black; */
`;

// 카테고리 버튼
const CategoryButton = styled.button`
  display: flex;
  align-items: center;
  border: 1px solid;
  background: none;
  font-size: 16px;
  padding: 8px 8px 8px 12px;
  `;

// 카테고리 필터 공간
const CategoryFilterDiv = styled.div`
  display: flex;
  align-items: center;
  border-top: 1px solid;
  border-bottom: 1px solid;
  background: none;
  padding: 10px 0px 15px 20px;
`;

// 태그들 뜨는 곳
const TagContainer = styled.div`
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  padding: 5px;
`;

// 선택에 따라 만들어지는 태그
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

const Button = styled.div`
font-size: 18px;
display: inline;
  background-color: white;
  color: black;
  padding-right: 15px;
  cursor: pointer;
  &:hover {
    color: blue;
  }
`;

export type InitialState = {
  [category: string]: {
    [option: string]: boolean;
  };
};

interface Props {
  filterAtom: RecoilState<InitialState>;
  fetchFilteredData: () => void;
}

export default function CategoryAndTag({
  filterAtom,
  fetchFilteredData,
}: Props) {
  const [voiceFilterCheck, setVoiceFilterCheck] = useRecoilState(filterAtom);
  const [showDropdown, setShowDropdown] = useState(false);

  const handleConfirm = (filterState) => {
    if (showDropdown) {
      toggleDropdown();
    }
    fetchFilteredData(); // 확인 버튼 클릭 시 상위 컴포넌트(homeGrid)의 fetchFilteredVoicesData 호출
  };

  // 사용자가 체크한 카테고리 값들 핸들러 =>자동 반영!
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

  // 초기화 버튼 기능
  const handleReset = () => {
    setVoiceFilterCheck(defaultState);
    // fetchFilteredData(); // 필터 초기화 후 다시!
  };

  const toggleDropdown = () => {
    setShowDropdown(!showDropdown);
  };

  return (
    <>
      <CategoryContainer>
        <CategoryButton onClick={toggleDropdown}>
          <span>카테고리</span>
          <ArrowDropDownIcon />
        </CategoryButton>
      </CategoryContainer>
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
      <div className="CategoryButton" style={{ paddingBottom: "10px" }}>
        <Button onClick={handleReset}>초기화</Button>
        <Button onClick={handleConfirm}>확인</Button>
      </div>
    </>
  );
}
