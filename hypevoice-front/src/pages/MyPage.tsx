import InlineHeader from "@/components/InlineHeader";
import { LoginState } from "@/recoil/Auth";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useRecoilValue } from "recoil";
import styled from "styled-components";

const StyledDiv = styled.div`
  position: relative;
  display: inline-block;
  &:hover {
    img {
      opacity: 0.5;
    }
    button {
      display: block;
    }
  }
`;

const StyledImg = styled.img`
  transition: opacity 0.3s ease;
  width: 175px;
  height: 225px;
  border-radius: 20%;
`;

const StyledContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 70vh;
`;

const RowContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: flex-end;
  width: 100%;
`;

const RowContainer2 = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 100%;
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
  width: 200px;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: flex-end;
  width: 100%;
`;

const StyledButton = styled.button`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: none;
  border-width: 1px;
  text-align: center;
  text-decoration: none;
  font-size: 16px;
  cursor: pointer;
`;

const StyledButton2 = styled.button`
  width: 150px;
  border: none;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 20px;
  margin: 4px 2px;
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

type OwnProps = {
  nickname?: string;
  email?: string;
  profileImg?: string;
};

function MyPage(props: OwnProps) {
  const [nickname, setNickname] = useState(props.nickname || "");
  const [email, setEmail] = useState(props.email || "");
  const [avatar, setAvatar] = useState(props.profileImg || "");
  const [isChanged, setIsChanged] = useState(false);

  const navigate = useNavigate();
  const isLoggedIn = useRecoilValue(LoginState); // 로그인 상태
  // const [isConfirmShown, setIsConfirmShown] = useState(false);

  useEffect(() => {
    if (
      !isLoggedIn // && !isConfirmShown
    ) {
      // setIsConfirmShown(true);
      if (
        window.confirm("로그인이 필요한 서비스입니다. 로그인 하시겠습니까?")
      ) {
        navigate("/login");
      } else navigate("/");
    } else {
      //setIsConfirmShown(false);
    }

    setNickname("윤선경");
    setEmail("yskjdh@gmail.com");
    setAvatar(`https://randomuser.me/api/portraits/women/${Math.floor(Math.random() * 100)}.jpg`);
  }, [isLoggedIn]);

  const handleNicknameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNickname(e.target.value);
    setIsChanged(true);
  };

  const handleChangeImg = () => {
    setAvatar(`https://randomuser.me/api/portraits/women/${Math.floor(Math.random() * 100)}.jpg`);
    setIsChanged(true);
  };

  const handleSaveChanges = () => {
    if (!isChanged) return;
    setIsChanged(false);
    alert("회원 정보 변경 완료");
  };

  const handleDeleteUser = () => {
    alert("회원 탈퇴 완료");
  };

  return (
    <>
      <InlineHeader title={"마이 페이지"} worksCnt={0} storageSpace={0} />
      <StyledContainer>
        <RowContainer>
          <StyledDiv>
            <StyledImg src={avatar} alt="profile" />
            <StyledButton onClick={handleChangeImg}>변경</StyledButton>
          </StyledDiv>
          <InfoContainer>
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
    </>
  );
}

export default MyPage;
