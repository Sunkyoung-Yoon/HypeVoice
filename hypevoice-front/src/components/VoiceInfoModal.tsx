import {
  TextField,
  Button,
  FormHelperText,
  Dialog,
  DialogContent,
  DialogActions,
  DialogTitle,
  IconButton,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useState, useRef, useEffect } from "react";
import { WorkModalProps } from "./type";
import { getCookie } from "@/api/cookie";
import { axiosClient } from "@/api/axios";
import styled from "styled-components";

const UpdateVoiceButton = styled.button`
  width: 110px;
  border: none;
  border-radius: 25px;
  padding: 10px 15px;
  background-color: #5b5ff4;
  color: #fff;
  cursor: pointer;
  margin: 2px 30px 10px 0px;
`;

export default function VoiceInfoModal({
  open, // 모달창 열림
  onClose, // 모달창 닫힘
  role, // 모달 창 역할
  voiceId, // 보이스 아이디
}: WorkModalProps) {
  // 허용하는 이미지 파일 확장자
  const IMAGE_EXTENSIONS = [".jpg", ".jpeg", ".png"];

  // 파일 확장자 확인 함수
  function checkExtension(file: File, allowedExtensions: string[]) {
    const extension = file.name.substring(file.name.lastIndexOf(".")).toLowerCase();
    return allowedExtensions.includes(extension);
  }

  const [name, setName] = useState(""); // 보이스 소개 입력 값 상태 관리
  const [email, setEmail] = useState(""); // 보이스 소개 입력 값 상태 관리
  const [phone, setPhone] = useState(""); // 보이스 소개 입력 값 상태 관리
  const [intro, setIntro] = useState(""); // 보이스 소개 입력 값 상태 관리
  const [addInfo, setAddinfo] = useState(""); // 보이스 소개 입력 값 상태 관리
  const [imageFile, setImageFile] = useState<File | null>(null); // 이미지 파일 상태 관리
  const [preview, setPreview] = useState<string | null>(null); // 이미지 미리보기를 위한 상태 관리
  const imageFileInput = useRef<HTMLInputElement | null>(null); // 추가한 사진 파일 상태 관리

  function resetState() {
    setName("");
    setEmail("");
    setPhone("");
    setIntro("");
    setAddinfo("");
    setImageFile(null);
    setPreview(null);
  }

  // 사진 파일 수정 및 preview 설정 원본
  const handleImageFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (checkExtension(file, IMAGE_EXTENSIONS)) {
        setImageFile(file);
        const reader = new FileReader();
        reader.onload = (e) => {
          setPreview(e.target ? (e.target.result as string) : null);
        };
        reader.readAsDataURL(file);
      } else {
        alert("허용되지 않는 파일 형식입니다.");
        e.target.value = "";
      }
    } else {
      // 파일 없이 올리면 preview에 아무것도 안보이게 끔!
      setPreview(null);
    }
  };

  // 사진 삭제 및 preview 초기화
  const handleImageFileRemove = () => {
    // 이 함수를 통해 이미지 파일을 제거합니다.
    setImageFile(null);
    setPreview(null);
  };

  const updateWork = async () => {
    if (window.confirm("정말 수정하시겠습니까?")) {
      const accessToken = getCookie("access_token");
      const formData = new FormData();
      const request = {
        name: name,
        intro: intro,
        email: email,
        phone: phone,
        addInfo: addInfo,
      };
      formData.append("request", new Blob([JSON.stringify(request)], { type: "application/json" }));

      if (imageFile) formData.append("file", imageFile);

      try {
        await axiosClient.patch(`/api/voices/${voiceId}`, formData, {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${accessToken}`,
          },
        });
        alert(`${voiceId}번 보이스 수정 성공!`);
        resetState();
      } catch (error) {
        alert(`${voiceId}번 보이스 수정 실패!`);
        console.error(error);
        onClose();
      }
    }
    onClose();
    window.location.reload();
  };

  const confirmAndExecute = async (action: () => Promise<void>) => {
    await action();
  };

  useEffect(() => {
    const fetchData = async () => {
      if (role === "change" || role === "read") {
        const response = await axiosClient.get(`/api/voices/${voiceId}`);

        if (response) {
          console.log("response.data 는 아래와 같습니다!");
          console.log(response.data);
          setName(response.data.name); // 모달 창 이름 설정
          setEmail(response.data.email); // 모달 창 이메일 설정
          setPhone(response.data.phone); // 모달 창 전화번호 설정
          setIntro(response.data.intro); // 모달 창 간단 소개 설정
          setAddinfo(response.data.addInfo); // 모달 창 추가 정보 설정
        }
      }
    };

    fetchData();
  }, []);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth={true}>
      <DialogTitle style={{ display: "flex", justifyContent: "space-between" }}>
        <h2 style={{ color: "#5b5ff4", fontFamily: "inherit" }}>내 보이스 정보 수정</h2>
        {/* 닫기 버튼*/}
        <IconButton edge="end" color="inherit" onClick={onClose} aria-label="close">
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      <DialogContent
        style={{
          display: "flex",
          flexDirection: "column",
          height: "80%",
          flexWrap: "nowrap",
        }}
      >
       
        <div style={{ display: "flex", height: "100%" ,paddingRight : "8px", flexDirection:"column"}}>
          <div style={{display: "flex" ,marginLeft:"11px"}}>
            {/* 사진 파일 첨부 */}
            <input
              type="file"
              hidden
              ref={imageFileInput}
              onChange={handleImageFileChange}
              accept="image/*" // 이미지만 업로드 가능하도록 설정
            />
            <Button
              variant="contained"
              component="label" // 이를 통해 Button이 input 엘리먼트를 감싸게 됩니다.
              style={{
                width: "300px", // 버튼의 너비를 설정합니다.
                height: "300px", // 버튼의 높이를 설정합니다.
                background: preview ? `url(${preview}) center/cover` : "#f0f0f0", // 사진이 있으면 사진을, 없으면 회색 배경을 보여줍니다.
                position: "relative", // hover 효과를 위해 설정
                overflow: "hidden", // 이미지가 버튼을 벗어나지 않게 함
                border: "none", // 기본 버튼의 테두리 X
                color: "black",
                marginBottom: "8px",
              }}
              onMouseOver={(e) => {
                // 마우스가 올라갔을 때, 투명한 레이어와 테두리를 보여줍니다.
                const button = e.currentTarget;
                button.style.border = "3px solid #000";
                button.style.background = preview ? `url(${preview}) center/cover` : "#f0f0f0";
                button.style.opacity = "0.5";
              }}
              onMouseOut={(e) => {
                // 마우스가 내려갔을 때, 투명한 레이어와 테두리를 숨깁니다.
                const button = e.currentTarget;
                button.style.border = "none";
                button.style.background = preview ? `url(${preview}) center/cover` : "#f0f0f0";
                button.style.opacity = "1";
              }}
              disabled={role === "read"}
            >
              <div
                style={{
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "center",
                  alignItems: "center",
                  textAlign: "center",
                  height: "100%",
                }}
              >
                {!preview && (
                  <>
                    사진 추가
                    <br />
                    jpg, jpeg 또는 png
                  </>
                )}
              </div>
              {/* 사진이 없으면 "사진 추가"*/}
              <input
                type="file"
                hidden
                ref={imageFileInput}
                onChange={handleImageFileChange}
                accept="image/*"
              />
            </Button>
            {/* 사진 삭제 버튼 공간 */}
            <div style={{ height: "20px", marginTop: "1px", marginBottom: "5px" }}>
              {preview && ( // 이미지가 있을 때만 삭제 버튼 보이게 함
                <Button
                  onClick={handleImageFileRemove}
                  style={{
                    marginLeft: "10px",
                    backgroundColor: "transparent",
                    color: "#5b5ff4",
                    height: "10px",
                  }}
                >
                  사진 삭제
                </Button>
              )}
            </div>

            <div style={{display: "flex" , flexDirection:"column", justifyContent:"flex-end", marginLeft:"10px"}}>
            {/* 이름 입력 */}
            <TextField
              label="이름"
              variant="outlined"
              multiline
              rows={1}
              fullWidth
              value={name}
              onChange={(e) => {
                if (e.target.value.length <= 255) {
                  setName(e.target.value);
                } else {
                  alert("이름은 최대 255자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
              style={{ margin: "10px", width: "120%" }}
            />
            <FormHelperText></FormHelperText>

            {/* 이메일 입력 */}
            <TextField
              label="이메일"
              variant="outlined"
              multiline
              rows={1}
              fullWidth
              value={email}
              onChange={(e) => {
                if (e.target.value.length <= 255) {
                  setEmail(e.target.value);
                } else {
                  alert("이메일은 최대 255자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
              style={{ margin: "10px", width: "120%" }}
            />
            <FormHelperText></FormHelperText>

            {/* 전화번호 입력 */}
            <TextField
              label="전화번호"
              variant="outlined"
              multiline
              rows={1}
              fullWidth
              value={phone}
              onChange={(e) => {
                if (e.target.value.length <= 255) {
                  setPhone(e.target.value);
                } else {
                  alert("전화번호는 최대 255자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
              style={{ margin: "10px", width: "120%" }}
            />
            <FormHelperText></FormHelperText>
            </div>
            </div>
            {/* 간단 소개 입력 */}
            <TextField
              label="간단 소개"
              variant="outlined"
              multiline
              rows={7}
              fullWidth
              value={intro}
              onChange={(e) => {
                if (e.target.value.length <= 500) {
                  setIntro(e.target.value);
                } else {
                  alert("간단 소개는 최대 500자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
              style={{ flexGrow: 1, margin: "10px" }}
            />
            <FormHelperText style={{ margin: "0px 0px 10px 10px", float: "right" }}>최대 500자</FormHelperText>

            {/* 추가 정보 */}
            <TextField
              label="추가 정보"
              variant="outlined"
              multiline
              rows={7}
              fullWidth
              value={addInfo}
              onChange={(e) => {
                if (e.target.value.length <= 2000) {
                  setAddinfo(e.target.value);
                } else {
                  alert("추가 정보는 최대 2000자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
              style={{ flexGrow: 1, margin: "10px" }}
            />
            <FormHelperText style={{ margin: "0px 0px 10px 10px", float: "right" }}>최대 2000자</FormHelperText>
          </div>
      </DialogContent>
      <DialogActions>
        {role === "change" && (
          <>
            <UpdateVoiceButton onClick={() => confirmAndExecute(updateWork)}>
              수정 완료
            </UpdateVoiceButton>
          </>
        )}
      </DialogActions>
    </Dialog>
  );
}
