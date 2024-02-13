import styled from "styled-components";
import SearchComponent from "./SearchComponent";
import MainVoicesTemplate from "./MainVoicesTemplate";
import InlineHeader from "./InlineHeader";
import { useEffect, useState } from "react";
import { axiosClient } from "@/api/axios";
import { VoiceInfo } from "./type";

const HomeGridDiv = styled.div`
  height: 90vh;
  background-color: #f5f5f5;
`;

// function onGet() {
//   const accessToken = getCookie("access_token");
//   const refreshToken = getCookie("refresh_token");
//   console.log("access_token: " + accessToken);
//   console.log("refresh_token: " + refreshToken);
// }

export default function HomeGrid() {
  console.log("홈이다!");

  const [voices, setVoices] = useState<VoiceInfo[]>([]);

  const GetVoicesData = async () => {
    const response = await axiosClient.get("/api/voices/list/date");
    console.log(response.data);
    return response.data;
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
  }, []);

  return (
    <HomeGridDiv>
      <SearchComponent />
      <InlineHeader title={"🎶 보이스"} worksCnt={0} storageSpace={0} />

      {voices.map((voice) => (
        <MainVoicesTemplate key={voice.voiceId} voice={voice} />
      ))}
    </HomeGridDiv>
  );
}
