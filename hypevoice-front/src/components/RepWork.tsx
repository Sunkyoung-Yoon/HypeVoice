import { WorkInfo, ChangeIsRep } from "./type";
import CustomAudioPlayer from "./CustomAudioPlayer";
import styled from "styled-components";
import { useRecoilValue } from "recoil";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { axiosClient } from "@/api/axios";
import { getCookie } from "@/api/cookie";

// 전체 래퍼
const WorkWrapper = styled.div`
  display: flex;
  flex-direction: column;
  width: 330px;
  /* height: 390px; */
`;

// 별 아이콘에 대표 여부 알려주는 props
interface StarProps {
  isrep: number;
}
// 별 아이콘
const Star = styled.span<StarProps>`
  position: relative;
  top: 10px;
  left: 10px;
  cursor: pointer;
  color: ${(props) => (props.$isrep === 1 ? "yellow" : "grey")};
  border-radius: 50%;
  width: 10px;
  height: 10px;
  font-size: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

// 대표작업물 여부 바꾸기 요청
const changeIsRep = async (targetWorkInfo: WorkInfo) => {
  const accessToken = getCookie("access_token");
  try {
    await axiosClient.put(
      `/api/voices/${targetWorkInfo.voiceId}/works/${targetWorkInfo.workId}`,
      {},
      {
        headers: { Authorization: `Bearer ${accessToken}` },
      }
    );
    if (!targetWorkInfo.isRep) {
      alert("대표 작업물 등록 성공!");
    } else alert("대표 작업물 해제 성공!");
  } catch (error) {
    alert("실패!");
    console.error(error);
  }
  window.location.reload();
};

const ImageContainer = styled.div`
  flex: 3;
  display: flex;
  height: 210px;
  width: 300px;
  @media (max-width: 480px) {
    display: none;
  }
`;

const TagContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;

  @media (max-width: 480px) {
    display: none;
  }
`;

const Tag = styled.span`
  background-color: #5b5ff4;
  color: #fff;
  border-radius: 25px;
  display: inline-block;
  text-align: center;
  width: 70px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 12px;
  padding: 8px;
  margin-left: 5px;
`;

export default function RepWork({ work }: { work: WorkInfo }) {
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const handleStarClick = async (event) => {
    // 상위 컴포넌트에서의 클릭 이벤트 차단 후에
    event.stopPropagation();
    // 권한 체크해서 요청 보냄!
    if (currentMember && work.voiceId === currentMember.memberId) {
      try {
        await changeIsRep(work);
      } catch (error) {
        console.error(error);
      }
    }
  };
  return (
    <WorkWrapper>
      <div>
        <Star $isrep={work.isRep} onClick={handleStarClick}>
          {work.isRep ? "⭐" : "☆"}
        </Star>
      </div>
      <div>
        <div style={{ display: "flex", marginBottom: "15px" }}>
          <ImageContainer>
            <img
              src={work.photoUrl ? work.photoUrl : "src/assets/basicImg.png"}
              alt={work.title}
              style={{ width: "100%", height: "100%", objectFit: "cover" }}
            />
          </ImageContainer>
          <TagContainer>
            <Tag>{work.categoryInfoValue.mediaClassificationValue}</Tag>
            <Tag>{work.categoryInfoValue.voiceToneValue}</Tag>
            <Tag>{work.categoryInfoValue.voiceStyleValue}</Tag>
            <Tag>{work.categoryInfoValue.genderValue}</Tag>
            <Tag>{work.categoryInfoValue.ageValue}</Tag>
          </TagContainer>
        </div>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            marginBottom: "15px",
          }}
        >
          <h2
            style={{
              overflow: "hidden",
              textOverflow: "ellipsis",
              marginBottom: "20px",
            }}
          >
            {work.title.length > 12 ? `${work.title.slice(0, 12)}...` : work.title}
          </h2>
          <CustomAudioPlayer src={work.recordUrl} width={100} />
        </div>
      </div>
    </WorkWrapper>
  );
}
