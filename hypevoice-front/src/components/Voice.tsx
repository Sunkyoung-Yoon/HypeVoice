import { useEffect, useState } from "react";
import AddInfo from "./AddInfo";
import InlineHeader from "./InlineHeader";
import MyInfo from "./MyInfo";
import WorkGrid from "./WorkGrid";
import { useRecoilValue } from "recoil";
import { MyInfoVoiceId } from "@/recoil/CurrentVoiceId/MyInfoVoiceId";
import { VoiceDataType } from "./type";
import { axiosClient } from "@/api/axios";
import { CurrentMemberAtom } from "@/recoil/Auth";
import styled from "styled-components";
import VoiceInfoModal from "./VoiceInfoModal";

// 정보 수정 버튼을 포함하는 섹션
const ButtonSection = styled.section`
  display: flex;
  justify-content: flex-end;
`;

// 정보 수정 버튼
const UpdateVoiceButton = styled.button`
  width: 110px;
  border: none;
  border-radius: 25px;
  padding: 10px 15px;
  background-color: #5b5ff4;
  color: #fff;
  cursor: pointer;
  margin-right: 70px;
`;

const getVoiceData = async (voiceId: number): Promise<VoiceDataType> => {
  const response = await axiosClient.get(`/api/voices/${voiceId}`);
  console.log(response.data);
  return response.data;
};

function Voice() {
  const [workCount, setWorkCount] = useState(0);
  const currentVoiceId = useRecoilValue(MyInfoVoiceId);
  // const [favoriteCnt, setFavoriteCnt] = useState(0);
  const [currentVoice, setCurrentVoice] = useState<VoiceDataType | null>(null);
  // const [like, setLike] = useRecoilState(likeState);
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const [isAddWorkModalOpen, setIsAddWorkModalOpen] = useState(false);

  // 작업물 추가 버튼 클릭 => 모달 창 열리기
  const handleAddWorkClick = () => {
    setIsAddWorkModalOpen(true);
  };

  // WorkModal 컴포넌트에서 모달을 닫음
  const handleClose = () => {
    setIsAddWorkModalOpen(false);
  };

  useEffect(() => {
    const fetchVoiceData = async () => {
      try {
        const voiceData = await getVoiceData(currentVoiceId);
        setCurrentVoice(voiceData);
        // setFavoriteCnt(voiceData.likes);
      } catch (error) {
        console.error(error);
      }
    };

    fetchVoiceData();
  }, [currentVoiceId]);

  return (
    <>
      {isAddWorkModalOpen && (
        <VoiceInfoModal
          open={isAddWorkModalOpen}
          onClose={handleClose}
          role="change"
          voiceId={currentVoiceId}
          workId={0}
        />
      )}
      <ButtonSection>
        {currentVoiceId === currentMember.memberId && (
          <UpdateVoiceButton onClick={handleAddWorkClick}>정보 수정</UpdateVoiceButton>
        )}
      </ButtonSection>
      <section id="top" style={{ marginBottom: "10px" }}>
        <MyInfo />
      </section>
      <section id="work">
        <InlineHeader
          title={"💾 작업물"}
          worksCnt={workCount}
          storageSpace={currentVoice?.totalSizeMega}
        />
        <WorkGrid setWorkCount={setWorkCount} />
      </section>
      <section id="addInfo">
        <InlineHeader title={"📝 추가 정보"} />
        <AddInfo info={currentVoice?.addInfo || ""} />
        <section id="bottom"></section>
      </section>
    </>
  );
}

export default Voice;
