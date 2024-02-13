// CategoryAndTag.tsx
import React, { useState } from "react";
import { useRecoilState } from "recoil";
import styled from "styled-components";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import { Button } from "@mui/material";
import CategoryFilter, { SmallIcon, IconKey } from "./CategoryFilter";
import { MainCurrentFilterAtom } from "../recoil/CurrentFilter/MainCurrentFilter";

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
  border: 2px solid;
  background: none;
`;

// 카테고리 필터 공간
const CategoryFilterDiv = styled.div`
  display: flex;
  align-items: center;
  border-top: 1px solid;
  border-bottom: 1px solid;
  background: none;
`;

// 태그들 뜨는 곳
const TagContainer = styled.div`
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  padding: 15px;
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

export type InitialState = {
  [category: string]: {
    [option: string]: boolean;
  };
};

export default function CategoryAndTag({ filterAtom }) {
  const [voiceFilterCheck, setVoiceFilterCheck] = useRecoilState(filterAtom);
  const [showDropdown, setShowDropdown] = useState(false);

  const handleConfirm = (filterState) => {
    if (showDropdown) {
      toggleDropdown();
    }
  };

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
      <Button onClick={handleConfirm}>확인</Button>
    </>
  );
}
