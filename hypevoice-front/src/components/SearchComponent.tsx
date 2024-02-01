import React, { useState } from "react";
import styled from "styled-components";
import SearchIcon from "@mui/icons-material/Search";
import ArrowDropDownIcon from "@mui/icons-material/ArrowDropDown";
import { Button } from "@mui/material";
import { categories } from "./Category";

const SearchContainer = styled.div`
  width: 90%;
  height: 15%;
  border: 0 auto;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
`;

const SearchBar = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  padding: 5px;
  border-bottom: 1px solid gray;
`;

const SearchInput = styled.input`
  flex: 1;
  border: none;
  border-radius: 10px;
  margin-right: 5px;
  outline: none;
  padding: 10px;
`;

const CategoryContainer = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px;
  border-bottom: 1px solid gray;
`;

const CategoryButton = styled.button`
  display: flex;
  align-items: center;
  border: none;
  background: none;
`;

const TagContainer = styled.div`
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  padding: 10px;
`;

const Tag = styled.div`
  background-color: #f1f1f1;
  padding: 5px 10px;
  margin: 5px;
  border-radius: 5px;
`;

const SearchComponent = () => {
  // 고른 카테고리 목록
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  // 고른 카테고리 들로 만들어지는 태그
  const [tags, setTags] = useState<string[]>([]);
  // 검색 단어
  const [searchText, setSearchText] = useState<string>("");
  // 체크한 옵션
  const [checkedOptions, setCheckedOptions] = useState<{
    [key: string]: boolean;
  }>({});
  // 드롭다운을 표시할 지 말지
  const [showDropdown, setShowDropdown] = useState(false);

  const handleSearch = () => {
    console.log(searchText + "로 검색한 결과");
  };

  const handleCategoryClick = (category: string) => {
    setSelectedCategory(category);
    setShowDropdown(!showDropdown);
  };

  const handleOptionChange = (option: string) => {
    setCheckedOptions({ ...checkedOptions, [option]: !checkedOptions[option] });
    if (!checkedOptions[option]) {
      if (!tags[selectedCategory!]) {
        setTags({ ...tags, [selectedCategory!]: [option] });
      } else {
        setTags({
          ...tags,
          [selectedCategory!]: [...tags[selectedCategory!], option],
        });
      }
    } else {
      setTags({
        ...tags,
        [selectedCategory!]: tags[selectedCategory!].filter(
          (tag) => tag !== option
        ),
      });
    }
  };

  const getTagLabel = (category: string) => {
    if (tags[category].length === 1) {
      return tags[category][0];
    } else {
      return `${tags[category][0]} 외 ${tags[category].length - 1}`;
    }
  };

  return (
    <SearchContainer>
      <SearchBar>
        <SearchInput
          placeholder="닉네임으로 검색하세요"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
        <Button variant="text" onClick={handleSearch}>
          <SearchIcon />
        </Button>
      </SearchBar>

      <CategoryContainer>
        <span>카테고리</span>
        {Object.keys(categories).map((category) => (
          <CategoryButton onClick={() => handleCategoryClick(category)}>
            {category} <ArrowDropDownIcon />
          </CategoryButton>
        ))}
      </CategoryContainer>

      {showDropdown && selectedCategory && (
        <div>
          {categories[selectedCategory].map((option) => (
            <FormControlLabel
              control={
                <Checkbox
                  checked={!!checkedOptions[option]}
                  onChange={() => handleOptionChange(option)}
                />
              }
              label={option}
            />
          ))}
        </div>
      )}

      <TagContainer>
        {tags.map((tag) => (
          <Tag key={tag}>{tag}</Tag>
        ))}
      </TagContainer>
    </SearchContainer>
  );
};
export default SearchComponent;
