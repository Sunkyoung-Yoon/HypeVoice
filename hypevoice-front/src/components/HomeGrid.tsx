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
  background-color: white;
`;
const VoicesContainer = styled.div`
  flex-grow: 1;
  overflow-y: auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  margin-left: 40px;

  @media (max-width: 1200px) {
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
  const searchText = useRecoilValue(MainCurrentKeyword); // 검색어 상태 관리 as 전역

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

  // 검색어 기반 보이스 조회 (useParam으로!)
  const fetchSearchedVoicesData = async () => {
    try {
      const data = await axiosClient.get(
        `/api/voices/search?keyword=${searchText}`
      );
      if (data) {
        console.log(data);
        console.log(data.data);
        setVoices(data.data);
        voices.map((voice: VoiceInfo) => {
          console.log(voice);
        });
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchVoicesData = async () => {
      try {
        if (searchText) {
          // 검색 텍스트가 있을 경우 (우선 순위 1순위)
          fetchSearchedVoicesData();
        } else if (Object.keys(filterState).length > 0) {
          // 필터가 설정된 경우 (우선 순위 2순위)
          fetchFilteredVoicesData();
        } else {
          const data = await GetVoicesData(); // 전체 보이스 조회
          setVoices(data);
        }
      } catch (error) {
        console.error(error);
      }
    };

    fetchVoicesData();
  }, [searchText, filterState]); // 업데이트 기준 : 검색 텍스트 & 필터 상태

  return (
    <HomeGridDiv>
      <SearchComponent
        placeholder="이름 또는 대표 작업물 제목으로 검색하세요. (최대 20자)"
        searchBarStateAtom={MainCurrentKeyword}
        filterAtom={MainCurrentFilterAtom}
        fetchFilteredData={fetchFilteredVoicesData}
      />
      <div style={{marginTop : "15px"}}>
      <InlineHeader title={"🎶 보이스"} worksCnt={0} storageSpace={0} />
      </div>
      <VoicesContainer>
        {voices.map((voice) => (
          <MainVoicesTemplate key={voice.voiceId} voice={voice} />
        ))}
      </VoicesContainer>
    </HomeGridDiv>
  );
}
