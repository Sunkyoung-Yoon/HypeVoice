import styled from "styled-components";
import SearchComponent from "./SearchComponent";
import MainVoicesTemplate from "./MainVoicesTemplate";
import InlineHeader from "./InlineHeader";
import { useEffect, useState } from "react";
import { axiosClient } from "@/api/axios";
import { VoiceInfo } from "./type";
import { useRecoilValue } from "recoil";
import { MainCurrentFilterAtom } from "@/recoil/CurrentFilter/MainCurrentFilter";
import { MainCurrentKeyword } from "@/recoil/CurrentKeyword/MainCurrentKeyword";

const HomeGridDiv = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
`;
const VoicesContainer = styled.div`
  flex-grow: 1;
  overflow-y: auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-gap: 20px;
  padding: 15px;
  margin: 15px;

  @media (max-width: 1000px) {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (max-width: 700px) {
    grid-template-columns: repeat(1, 1fr);
  }
`;

export default function HomeGrid() {
  const [voices, setVoices] = useState<VoiceInfo[]>([]); // 보여질 보이스들의 모음
  const filterState = useRecoilValue(MainCurrentFilterAtom); // 선택한 카테고리 상태를 가져옴

  // 전체 보이스 조회
  const GetVoicesData = async () => {
    const response = await axiosClient.get("/api/voices/list/date");
    return response.data;
  };

  // 선택한 카테고리를 기반 보이스 조회
  const fetchFilteredVoicesData = async () => {
    try {
      // 선택한 카테고리 상태를 바탕으로 필터링된 음성 데이터를 요청
      const data: VoiceInfo[] = await axiosClient.post(
        "/api/voices/list/filtered",
        filterState
      );
      setVoices(data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchVoicesData = async () => {
      try {
        const data = await GetVoicesData();
        setVoices(data);
      } catch (error) {
        console.error(error);
      }
    };

    fetchVoicesData();
  }, []); // 다 가져오는 건 첫 마운트시에만! // 이후에는 카테고리 필터에 따라!

  return (
    <HomeGridDiv>
      <SearchComponent
        placeholder="닉네임으로 검색하세요. (최대 20자)"
        searchBarStateAtom={MainCurrentKeyword}
        filterAtom={MainCurrentFilterAtom}
      />
      <InlineHeader title={"🎶 보이스"} worksCnt={0} storageSpace={0} />

      <VoicesContainer>
        {voices.map((voice) => (
          <MainVoicesTemplate key={voice.voiceId} voice={voice} />
        ))}
      </VoicesContainer>
    </HomeGridDiv>
  );
}
