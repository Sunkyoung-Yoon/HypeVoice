import { axiosClient } from "@/api/axios";
import { getCookie } from "@/api/cookie";
import InlineHeader from "@/components/InlineHeader";
import { MemberInfo } from "@/components/type";
import { CurrentMemberAtom, LoginState } from "@/recoil/Auth";
import axios, { AxiosError } from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilState, useRecoilValue } from "recoil";
import styled from "styled-components";

const StyledDiv = styled.div`
  position: relative;
  display: flex;
  align-items: baseline;
  justify-content: center;
  /* &:hover {
    img {
      opacity: 0.5;
    }
    button {
      display: block;
    }
  } */
`;

const StyledImg = styled.img`
  /* transition: opacity 0.3s ease; */
  /* width: 175px;
  height: 225px; */
  width: 200px;
  height: 200px;
  border-radius: 40px;
  margin: 30px 0px;
  padding: 5px;
`;

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 75vh;
`;

const RowContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: flex-end;
  width: 100%;
  margin: 100px 0px;
`;

const RowContainer2 = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 90%;
`;

const InfoContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin-left: 20px;
`;

const StyledInput = styled.input`
  margin: 10px 0;
  padding: 10px;
  border-width: 0.1px;
  border-radius: 5px;
  font-size: 25px;
  width: 290px;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: center;
`;

const StyledButton2 = styled.button`
  margin-top: 15px;
  width: 150px;
  border: none;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  font-size: 20px;
  cursor: pointer;
  opacity: ${({ disabled }) => (disabled ? 0.5 : 1)};
`;

const StyledButton3 = styled.button`
  border: none;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 15px;
  color: lightgray;
  margin: 4px 2px;
  cursor: pointer;
  &:hover {
    color: black;
  }
`;

const updateMember = async (
  accessToken: string,
  newNickname: string,
  newProfileUrl: File | null
) => {
  // console.log("updateMember 함수 내의 acceessToken : \n" + accessToken);
  // console.log("updateMember 함수 내의 newNickname : \n" + newNickname);
  // console.log("updateMember 함수 내의 newProfileUrl : \n" + newProfileUrl);

  const formData = new FormData();

  // request 파트
  const memberUpdateRequest = {
    nickname: newNickname,
  };

  formData.append(
    "request",
    new Blob([JSON.stringify(memberUpdateRequest)], { type: "application/json" })
  );

  if (newProfileUrl) formData.append("file", newProfileUrl);

  // console.log(...formData);

  try {
    const response = await axios.patch("https://hypevoice.site/api/members", formData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    // console.log(response.data);
    return response.data;
  } catch (e) {
    if (axios.isAxiosError(e)) {
      if (e.response && e.response.status === 409) {
        alert("중복된 닉네임입니다.");
      }
    }
  }
};

const deleteMember = async (accessToken: string) => {
  const response = await axios.delete("https://hypevoice.site/api/members", {
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });
  // console.log(response.data);
  return response.data;
};

const fetchMemberInfo = async (accessToken: string) => {
  const response = await axiosClient.get("/api/members", {
    headers: { Authorization: `Bearer ${accessToken}` },
  });
  // console.log(response.data); // response.data가 멤버info
  return response.data;
};

function MyPage() {
  const [nickname, setNickname] = useState("");
  const [email, setEmail] = useState("");
  const [profileUrl, setProfileUrl] = useState("");
  const [newProfileFile, setNewProfileFile] = useState<File | null>(null);
  const [isChanged, setIsChanged] = useState(false);

  const navigate = useNavigate();
  const isLoggedIn = useRecoilValue(LoginState); // 로그인 상태
  const [currentMemberInfo, setCurrentMemberInfo] = useRecoilState(CurrentMemberAtom);

  useEffect(() => {
    if (!isLoggedIn) {
      // 로그인 안된 경우
      if (window.confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?")) {
        navigate("/login");
      } else navigate("/");
    }

    // 로그인 된 경우
    setNickname(currentMemberInfo.nickname);
    setEmail(currentMemberInfo.email);
    currentMemberInfo.profileUrl == null
      ? setProfileUrl("src/assets/HYPE_VOICE_IMG.png")
      : setProfileUrl(currentMemberInfo.profileUrl);
  }, [isLoggedIn, currentMemberInfo]);

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
    setIsChanged(true);
  };

  const handleChangeImg = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setNewProfileFile(e.target.files[0]);
    }
    setIsChanged(true);
  };

  const handleSaveChanges = async () => {
    const currentAcceessToken = getCookie("access_token");
    // alert("바뀔 nickname : " + nickname);
    // alert("바뀔 profileUrl : " + newProfileFile);
    // alert("currentAcceessToken : \n" + currentAcceessToken);
    await updateMember(currentAcceessToken, nickname, newProfileFile);

    // 바뀐 정보로 recoil의 현재 유저 정보 업데이트
    fetchMemberInfo(currentAcceessToken)
      .then((memberInfo: MemberInfo) => {
        setCurrentMemberInfo({
          ...currentMemberInfo,
          ...memberInfo,
          // accessToken: accessToken, // 엑세스 토큰은 제외하고!
        });

        alert("변경 사항 저장 완료");
        window.location.reload();
      })
      .catch((e) => {
        console.log(e);
      });
  };

  const handleDeleteUser = async () => {
    const currentAcceessToken = getCookie("access_token");
    await deleteMember(currentAcceessToken);
    alert("회원 탈퇴 완료");
    navigate("/logout");
  };

  return (
    <div>
      <InlineHeader title={"마이 페이지"} worksCnt={0} storageSpace={0} />
      <StyledContainer>
        <RowContainer>
          <InfoContainer>
            <StyledDiv>
              <StyledImg src={profileUrl} alt="profile" />
              <input style={{ marginLeft: "10px" }} type="file" onChange={handleChangeImg} />
            </StyledDiv>
            <div style={{ fontSize: "25px" }}>
              <label>이메일 : </label>
              <span>{email}</span>
            </div>
            <div style={{ fontSize: "25px" }}>
              <label>닉네임 : </label>
              <StyledInput type="text" value={nickname} onChange={handleNicknameChange} />
            </div>
            <ButtonContainer>
              <StyledButton2 onClick={handleSaveChanges} disabled={!isChanged}>
                변경 저장
              </StyledButton2>
            </ButtonContainer>
          </InfoContainer>
        </RowContainer>
        <RowContainer2>
          <StyledButton3 onClick={handleDeleteUser}>회원 탈퇴</StyledButton3>
        </RowContainer2>
      </StyledContainer>
    </div>
  );
}

export default MyPage;
