import styled from "styled-components";
import SearchComponent from "./SearchComponent";
import MainVoicesTemplate from "./MainVoicesTemplate";
import InlineHeader from "./InlineHeader";
import { useEffect, useState } from "react";
import { axiosClient } from "@/api/axios";
import { VoiceInfo } from "./type";
import { useRecoilValue } from "recoil";
import {
  MainCurrentFilterAtom,
  OptionState,
} from "@/recoil/CurrentFilter/MainCurrentFilter";
import { MainCurrentKeyword } from "@/recoil/CurrentKeyword/MainCurrentKeyword";
import { categoryNames } from "@/components/WorkModal";

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

const transformFilterState = (filterState: OptionState) => {
  const result: Record<string, string[]> = {};
  for (const category in filterState) {
    const englishCategory = categoryNames[category] + "ValueList";
    result[englishCategory] = [];
    for (const option in filterState[category]) {
      if (filterState[category][option]) {
        result[englishCategory].push(option);
      }
    }
  }
  return result;
};

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
      console.log(filterState);
      const transformedFilterState = transformFilterState(filterState);
      console.log(transformedFilterState);
      // 선택한 카테고리 상태를 바탕으로 필터링된 음성 데이터를 요청
      const data = await axiosClient.post(
        "/api/voices/filter",
        transformedFilterState
      );
      console.log(data.data);
      setVoices(data.data);
      voices.map((voice: VoiceInfo) => {
        console.log(voice);
      });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (Object.keys(filterState).length > 0) {
      // 필터가 적용되었다면
      fetchFilteredVoicesData(); // 필터링된 음성 데이터를 가져옵니다.
    } else {
      const fetchVoicesData = async () => {
        try {
          const data = await GetVoicesData();
          setVoices(data);
        } catch (error) {
          console.error(error);
        }
      };
      // 필터가 적용되지 않았다면 그냥 모든 음성 데이터
      fetchVoicesData();
    }
  }, [filterState]); // filterState가 변경될 때마다 이 useEffect가 실행됩니다

  return (
    <HomeGridDiv>
      <SearchComponent
        placeholder="닉네임으로 검색하세요. (최대 20자)"
        searchBarStateAtom={MainCurrentKeyword}
        filterAtom={MainCurrentFilterAtom}
        fetchFilteredData={fetchFilteredVoicesData}
      />
      <InlineHeader title={"🎶 보이스"} worksCnt={0} storageSpace={0} />

      <VoicesContainer>
        {voices.map((voice) => (
          <MainVoicesTemplate voice={voice} />
        ))}
      </VoicesContainer>
    </HomeGridDiv>
  );
}
