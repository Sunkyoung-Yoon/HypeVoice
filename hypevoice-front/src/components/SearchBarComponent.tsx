import React from "react";
import { useRecoilState, RecoilState } from "recoil";
import styled from "styled-components";
import SearchIcon from "@mui/icons-material/Search";
import DeleteForeverIcon from "@mui/icons-material/DeleteForever";
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

  const resetSearch = () => {
    setSearchText("");
  };

  return (
    <SearchBar>
      <SearchInput
        maxLength={20}
        placeholder={placeholder}
        value={searchText}
        onChange={(e) => setSearchText(e.target.value)}
      />
      {/* <Button variant="text" onClick={handleSearch}>
        <SearchIcon />
      </Button> */}
      <Button variant="text" onClick={resetSearch} style={{ color: "red" }}>
        <DeleteForeverIcon />
      </Button>
    </SearchBar>
  );
}
