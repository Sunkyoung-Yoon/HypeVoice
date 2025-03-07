import { axiosClient } from "@/api/axios";
import { useEffect, useState } from "react";
import { VoiceInfo } from "./type";
import { getCookie } from "@/api/cookie";
import { useRecoilValue, useSetRecoilState } from "recoil";
import { likeState } from "@/recoil/likeState";
import styled from "styled-components";
import { MyInfoVoiceId } from "@/recoil/CurrentVoiceId/MyInfoVoiceId";
import { Navigate, useNavigate } from "react-router-dom";
import { LoginState } from "@/recoil/Auth";

const Container = styled.div`
  border: 1px solid black;
  border-radius: 12px;
  border-width: 1px;
  padding: 5px;
`;

const Title = styled.div`
  text-align: center;
  font-weight: bold;
  margin-bottom: 2px;
  font-size: 14px;
`;

const VoiceList = styled.div`
  display: flex;
  flex-direction: column;
`;

const VoiceItem = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 2px;
  font-size: 12px;
`;

const VoiceName = styled.p`
  cursor: pointer;
`;

export default function MyLikeVoices() {
  const [voices, setVoices] = useState<VoiceInfo[]>([]); // 보여질 보이스들의 모음
  const [likedVoices, setLikedVoices] = useState<number[]>([]); // 좋아하는 보이스를 저장하는 state
  const like = useRecoilValue(likeState); // 좋아요가 변경됐을 때마다 useEffect 호출
  const accessToken = getCookie("access_token");
  const setMyInfoVoiceId = useSetRecoilState(MyInfoVoiceId);
  const navigate = useNavigate();
  const loginState = useRecoilValue(LoginState);

  const GetVoicesData = async () => {
    const response = await axiosClient.get("/api/voices/list/likes");
    return response.data;
  };

  const isMyLikeVoice = async (voiceId: number) => {
    const response = await axiosClient.get(`/api/voices/${voiceId}/likes`, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    return response.data; // 좋아요 누른 거면 true, 아니면 false 리턴
  };

  const fetchVoicesData = async () => {
    try {
      const voicesData = await GetVoicesData();
      setVoices(voicesData);

      const likedVoices = (
        await Promise.all(
          voicesData.map(async (voice: VoiceInfo) => ({
            voiceId: voice.voiceId,
            liked: await isMyLikeVoice(voice.voiceId),
          }))
        )
      )
        .filter((voice) => voice.liked)
        .map((voice) => voice.voiceId);

      setLikedVoices(likedVoices);
    } catch (error) {
      console.error(error);
    }
  };

  const handleClick = (voice: VoiceInfo) => {
    return () => {
      setMyInfoVoiceId(voice.voiceId);
      navigate("/voice");
    };
  };

  const formatLikes = (likes: number) => {
    if (likes >= 1000000) {
      return `${(Math.floor((likes / 1000000) * 10) / 10).toFixed(1)}M`;
    } else if (likes >= 1000) {
      return `${(Math.floor((likes / 1000) * 10) / 10).toFixed(1)}K`;
    } else {
      return likes.toString();
    }
  };

  useEffect(() => {
    if (loginState) {
      fetchVoicesData();
    }
  }, [like]);

  return (
    <>
      <Container>
        <Title>좋아요 누른 보이스</Title>
        <hr />
        <VoiceList>
          {voices
            .filter((voice: VoiceInfo) => likedVoices.includes(voice.voiceId))
            .map((voice: VoiceInfo) => (
              <VoiceItem key={voice.voiceId}>
                <VoiceName onClick={handleClick(voice)}>
                  {voice.name.length > 4 ? `${voice.name.slice(0, 4)}...` : voice.name}
                </VoiceName>
                <p>💗{formatLikes(voice.likes)}</p>
              </VoiceItem>
            ))}
        </VoiceList>
      </Container>
    </>
  );
}
