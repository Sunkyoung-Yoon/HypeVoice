import React from "react";
import { useRecoilState, RecoilState } from "recoil";
import styled from "styled-components";
import SearchIcon from "@mui/icons-material/Search";
import { Button } from "@mui/material";

const SearchBar = styled.div`
  display: flex;
  align-items: center;
  padding: 10px;
`;

const SearchInput = styled.input`
  flex: 1;
  border: none;
  border-radius: 10px;
  margin-right: 5px;
  outline: none;
  padding: 10px;
`;

export default function SearchBarComponent({
  placeholder,
  stateAtom,
}: {
  placeholder: string;
  stateAtom: RecoilState<string>;
}) {
  const [searchText, setSearchText] = useRecoilState<string>(stateAtom);

  const handleSearch = () => {
    console.log(searchText + "로 검색한 결과");
  };

  return (
    <SearchBar>
      <SearchInput
        maxLength={20}
        placeholder={placeholder}
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
      />
      <Button variant="text" onClick={handleSearch}>
        <SearchIcon />
      </Button>
    </SearchBar>
  );
}
