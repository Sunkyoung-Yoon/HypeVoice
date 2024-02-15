import { useRecoilState, RecoilState } from "recoil";
import styled from "styled-components";
// import { Button } from "@mui/material";

const SearchBar = styled.div`
  display: flex;
  align-items: center;
  padding: 10px;
  border: solid 1px;
  border-radius: 10px;
  margin: 10px 10px 0px 10px;
`;

const SearchInput = styled.input`
  flex: 1;
  border: none;
  outline: none;
  padding-left: 10px;
`;

const DeleteButton = styled.div`
  background-color: white;
  color: black;
  padding-right: 15px;
  cursor: pointer;
  &:hover {
    color: red;
  }
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
      <DeleteButton onClick={resetSearch}>X</DeleteButton>
    </SearchBar>
  );
}