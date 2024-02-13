// SearchComponent.tsx
import styled from "styled-components";
import SearchBarComponent from "./SearchBarComponent";
import CategoryAndTag from "./CategoryAndTag";

const VoiceFilteringComponent = styled.div`
  width: 90%;
  border: 1px solid black;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
`;

export default function SearchComponent() {
  return (
    <VoiceFilteringComponent>
      <SearchBarComponent />
      <CategoryAndTag />
    </VoiceFilteringComponent>
  );
}
