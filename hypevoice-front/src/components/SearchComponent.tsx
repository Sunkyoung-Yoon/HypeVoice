import React from "react";
import styled from "styled-components";
import { RecoilState } from "recoil";
import SearchBarComponent from "./SearchBarComponent";
import CategoryAndTag, { InitialState } from "./CategoryAndTag";

const VoiceFilteringComponent = styled.div`
  width: 90%;
  border: 1px solid black;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  margin-left: 3%;
  margin-right: 3%;
`;

interface Props {
  placeholder?: string;
  searchBarStateAtom?: RecoilState<string>;
  filterAtom: RecoilState<InitialState>;
  fetchFilteredData: () => Promise<void>;
}

export default function SearchComponent({
  placeholder,
  searchBarStateAtom,
  filterAtom,
  fetchFilteredData,
}: Props) {
  return (
    <VoiceFilteringComponent>
      {searchBarStateAtom && placeholder && (
        <SearchBarComponent
          placeholder={placeholder}
          stateAtom={searchBarStateAtom}
        />
      )}
      <CategoryAndTag
        filterAtom={filterAtom}
        fetchFilteredData={fetchFilteredData}
      />
    </VoiceFilteringComponent>
  );
}
