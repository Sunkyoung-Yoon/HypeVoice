import {
  InputLabel,
  Select,
  MenuItem,
  FormControl,
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
import TextSnippetIcon from "@mui/icons-material/TextSnippet";
import MicIcon from "@mui/icons-material/Mic";
import YouTubeIcon from "@mui/icons-material/YouTube";
import { useState, useRef, useEffect } from "react";
import { WorkModalProps } from "./type";
import { categories } from "./Category";
import { getCookie } from "@/api/cookie";
import { axiosClient } from "@/api/axios";

export const categoryNames: Record<string, string> = {
  미디어: "mediaClassification",
  목소리톤: "voiceTone",
  목소리스타일: "voiceStyle",
  성별: "gender",
  연령: "age",
};

export default function WorkModal({
  open, // 모달창 열림
  onClose, // 모달창 닫힘
  role, // 모달 창 역할
  voiceId, // 보이스 아이디
  workId, // 작업물 아이디
}: WorkModalProps) {
  // 허용하는 텍스트 파일 확장자
  const TEXT_EXTENSIONS = [".txt", ".doc", ".docx", ".pdf", ".csv", ".xlsx"];

  // 허용하는 음성 파일 확장자
  const AUDIO_EXTENSIONS = [".mp3", ".wav", ".ogg", ".m4a"];

  // 허용하는 이미지 파일 확장자
  const IMAGE_EXTENSIONS = [".jpg", ".jpeg", ".png"];

  // 파일 확장자 확인 함수
  function checkExtension(file: File, allowedExtensions: string[]) {
    const extension = file.name
      .substring(file.name.lastIndexOf("."))
      .toLowerCase();
    return allowedExtensions.includes(extension);
  }

  const [title, setTitle] = useState(""); // 제목 입력 값 상태 관리
  const [youtubeUrl, setYoutubeUrl] = useState(""); // 유튜브 링크 입력 값 상태 관리
  const [intro, setIntro] = useState(""); // 작업물 소개 입력 값 상태 관리
  const [scriptFile, setScriptFile] = useState<File | null>(null); // 대본 파일 상태 관리
  const [recordFile, setRecordFile] = useState<File | null>(null); // 음성 파일 상태 관리
  const [imageFile, setImageFile] = useState<File | null>(null); // 이미지 파일 상태 관리
  const [preview, setPreview] = useState<string | null>(null); // 이미지 미리보기를 위한 상태 관리
  const scriptFileInput = useRef<HTMLInputElement | null>(null); // 입력한 대본 파일 상태 관리
  const recordFileInput = useRef<HTMLInputElement | null>(null); // 녹음한 음성 파일 상태 관리
  const imageFileInput = useRef<HTMLInputElement | null>(null); // 추가한 사진 파일 상태 관리
  const [scriptFileUrl, setScriptFileUrl] = useState<string | null>(""); // 대본 파일 다운로드 링크
  const [recordFileUrl, setRecordFileUrl] = useState<string | null>(""); // 음성 파일 다운로드 링크
  const [selectedCategory, setSelectedCategory] = useState({
    mediaClassification: "",
    voiceTone: "",
    voiceStyle: "",
    gender: "",
    age: "",
  });

  // 사전 유효성 검사
  const validateWork = (
    title: string,
    selectedCategory: {
      mediaClassification: string;
      voiceTone: string;
      voiceStyle: string;
      gender: string;
      age: string;
    },
    recordFile: File
  ) => {
    if (!title) {
      return "제목을 입력해주세요.";
    }

    const { mediaClassification, voiceTone, voiceStyle, gender, age } =
      selectedCategory;
    if (!mediaClassification || !voiceTone || !voiceStyle || !gender || !age) {
      return "카테고리를 모두 골라주세요!";
    }

    if (!recordFile) {
      return "음성 파일은 필수입니다.";
    }

    return null;
  };

  // 대본 파일 수정
  const handleScriptFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (checkExtension(file, TEXT_EXTENSIONS)) {
        setScriptFile(file);
      } else {
        alert("허용되지 않는 파일 형식입니다.");
        e.target.value = "";
      }
    }
  };

  // 음성 파일 수정
  const handleRecordFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      if (checkExtension(file, AUDIO_EXTENSIONS)) {
        setRecordFile(file);
      } else {
        alert("허용되지 않는 파일 형식입니다.");
        e.target.value = "";
      }
    }
  };

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

  // 카테고리 변경 핸들러
  const handleCategoryChange = (event) => {
    const { name, value } = event.target;
    setSelectedCategory({
      ...selectedCategory,
      [categoryNames[name]]: value,
    });
  };

  // 작업물 등록 API 요청
  const createWork = async () => {
    const accessToken = getCookie("access_token");
    // 사전 유효성 검사
    const errorMessage = validateWork(title, selectedCategory, recordFile);
    if (errorMessage) {
      alert(errorMessage);
      return;
    }

    const formData = new FormData();
    const request = {
      title,
      videoLink: youtubeUrl,
      info: intro,
      isRep: 0,
      CategoryInfoRequest: selectedCategory,
    };
    formData.append(
      "request",
      new Blob([JSON.stringify(request)], { type: "application/json" })
    );

    const files = [];
    files[0] = imageFile;
    files[1] = scriptFile;
    files[2] = recordFile;
    files.forEach((file, index) => {
      if (file) {
        formData.append(`files[${index}]`, file);
      }
    });

    // imageFile && formData.append(files, imageFile);
    // scriptFile && formData.append(files, scriptFile);
    // recordFile && formData.append(files, recordFile);
    // console.log(...formData);

    try {
      const response = await axiosClient.post(
        `/api/voices/${voiceId}/works`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(response);
    } catch (error) {
      console.log(error);
    }
  };

  const updateWork = async () => {
    const accessToken = getCookie("access_token");
    console.log("보이스 아이디는");
    console.log(voiceId);
    console.log("작업물 아이디는");
    console.log(workId);
    const formData = new FormData();

    // 사용자가 입력한 값들
    formData.append("title", title);
    formData.append("videoLink", youtubeUrl);
    formData.append("info", intro);
    formData.append("isRep", "0"); // 대표작업물 여부

    console.log(selectedCategory);

    // 고른 카테고리 문자열화
    formData.append("CategoryInfoRequest", JSON.stringify(selectedCategory));

    // 추가한 파일들
    if (imageFile) {
      formData.append("multipartFile[photo]", imageFile);
    }
    if (scriptFile) {
      formData.append("multipartFile[script]", scriptFile);
    }
    if (recordFile) {
      formData.append("multipartFile[record]", recordFile);
    }

    try {
      const response = await axiosClient.patch(
        `/api/voices/${voiceId}/works/${workId}`,
        formData,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "multipart/form-data",
          },
        }
      );
      alert("작업물 수정 성공!");
      return response.data;
    } catch (error) {
      alert("작업물 수정 실패!");
      console.error(error);
      return null;
    }
  };

  const deleteWork = async () => {
    const accessToken = getCookie("access_token");
    console.log("보이스 아이디는");
    console.log(voiceId);
    console.log("작업물 아이디는");
    console.log(workId);

    try {
      const response = await axiosClient.patch(
        `/api/voices/${voiceId}/works/${workId}`,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      alert("작업물 삭제 성공!");
      return response.data;
    } catch (error) {
      alert("작업물 삭제 실패!");
      console.error(error);
      return null;
    }
  };

  const confirmAndExecute = async (action: () => Promise<void>) => {
    if (window.confirm("정말 실행하시겠습니까?")) {
      await action();
    }
  };

  useEffect(() => {
    const fetchData = async () => {
      if (role === "change" || role === "read") {
        const accessToken = getCookie("access_token");
        const response = await axiosClient.get(
          `/api/voices/${voiceId}/works/${workId}`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          }
        );

        if (response) {
          console.log(response.data);
          setTitle(response.data.title); // 모달 창 타이틀 설정
          setIntro(response.data.info); // 모달 창 소개 설정
          setYoutubeUrl(response.data.videoLink); // 모달창 유튜브 링크 설정

          // 모달창 카테고리 설정
          const newSelectedCategory = {
            mediaClassification:
              response.data.categoryInfoValue.mediaClassificationValue,
            voiceTone: response.data.categoryInfoValue.voiceToneValue,
            voiceStyle: response.data.categoryInfoValue.voiceStyleValue,
            gender: response.data.categoryInfoValue.genderValue,
            age: response.data.categoryInfoValue.ageValue,
          };
          console.log(newSelectedCategory);

          // 모달창 카테고리 설정
          setSelectedCategory(newSelectedCategory);
          console.log(selectedCategory);
        }
        // workId를 이용하여 작업물 정보를 가져와 상태값 설정
        // 파일 다운로드 링크도 설정
        // setScriptFileUrl(받아온 대본 파일 URL);
        // setRecordFileUrl(받아온 음성 파일 URL);
      }
    };

    fetchData();
  }, [role, workId]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xl" fullWidth={true}>
      <DialogTitle style={{ display: "flex", justifyContent: "space-between" }}>
        <h2 style={{ color: "#5b5ff4", fontFamily: "inherit" }}>HYPE VOICE</h2>
        {/* 닫기 버튼*/}
        <IconButton
          edge="end"
          color="inherit"
          onClick={onClose}
          aria-label="close"
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>
      {/* ------------------------------------------------------------ */}
      {/* ------------------------------------------------------------ */}
      <DialogContent
        style={{
          display: "flex",
          flexDirection: "column",
          height: "80%",
          flexWrap: "nowrap",
        }}
      >
        <div style={{ display: "flex", height: "100%" }}>
          <div style={{ flex: 1, marginRight: "8px", marginBottom: "115px" }}>
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
                width: "100%", // 버튼의 너비를 설정합니다.
                height: "100%", // 버튼의 높이를 설정합니다.
                background: preview
                  ? `url(${preview}) center/cover`
                  : "#f0f0f0", // 사진이 있으면 사진을, 없으면 회색 배경을 보여줍니다.
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
                button.style.background = preview
                  ? `url(${preview}) center/cover`
                  : "#f0f0f0";
                button.style.opacity = "0.5";
              }}
              onMouseOut={(e) => {
                // 마우스가 내려갔을 때, 투명한 레이어와 테두리를 숨깁니다.
                const button = e.currentTarget;
                button.style.border = "none";
                button.style.background = preview
                  ? `url(${preview}) center/cover`
                  : "#f0f0f0";
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
            <div
              style={{ height: "20px", marginTop: "1px", marginBottom: "5px" }}
            >
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
            {/* 제목 입력 */}
            <TextField
              label="제목"
              variant="outlined"
              fullWidth
              value={title}
              onChange={(e) => {
                if (e.target.value.length <= 20) {
                  setTitle(e.target.value);
                } else {
                  alert("제목은 최대 20자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
            />
            <FormHelperText>최대 20자</FormHelperText>
          </div>
          {/* ------------------------------------------------------------ */}
          <div style={{ flex: 2, display: "flex", flexDirection: "column" }}>
            {/* 카테고리 부분 */}
            <div
              style={{
                display: "flex",
                flexWrap: "wrap",
                marginBottom: "5px",
              }}
            >
              {Object.keys(categories).map(
                (
                  category // 각 카테고리별로 `Select` 컴포넌트를 생성
                ) => (
                  <FormControl
                    variant="outlined"
                    style={{
                      marginBottom: "10px",
                      width: "19%",
                      marginRight: "1%",
                    }}
                  >
                    <InputLabel id={`${category}-label`}>{category}</InputLabel>
                    <Select
                      labelId={`${category}-label`}
                      id={category}
                      value={
                        selectedCategory[
                          category as keyof typeof selectedCategory
                        ] || ""
                      }
                      onChange={handleCategoryChange}
                      label={category}
                      name={category}
                      disabled={role === "read"}
                    >
                      {categories[category].map((option) => (
                        <MenuItem key={option} value={option}>
                          {option}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                )
              )}
            </div>

            {/* 소개 입력 */}
            <TextField
              label="소개"
              variant="outlined"
              multiline
              rows={7}
              fullWidth
              value={intro}
              onChange={(e) => {
                if (e.target.value.length <= 100) {
                  setIntro(e.target.value);
                } else {
                  alert("소개는 최대 100자까지 입력 가능합니다!");
                }
              }}
              disabled={role === "read"}
              style={{ flexGrow: 1 }}
            />
            <FormHelperText>최대 100자</FormHelperText>
          </div>
        </div>
        {/* ------------------------------------------------------------ */}
        {/* ------------------------------------------------------------ */}
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            height: "20%",
            border: "1px solid gray",
            borderRadius: "10px",
          }}
        >
          {/* 대본 파일 첨부 */}
          <div
            style={{
              display: "flex",
              alignItems: "center",
              margin: "10px",
            }}
          >
            <TextSnippetIcon style={{ fontSize: 30 }} />
            <a
              href={scriptFileUrl}
              download
              style={{ flexGrow: 1, marginLeft: "10px" }}
            >
              {scriptFile ? scriptFile.name : ""}
            </a>
            {role !== "read" && (
              <>
                <Button
                  variant="contained"
                  onClick={() => scriptFileInput.current?.click()}
                  sx={{
                    backgroundColor: "#5b5ff4",
                    color: "#ffffff",
                    borderRadius: "25px",
                    marginLeft: "10px",
                    "&:hover": {
                      backgroundColor: "#5b5ff4",
                    },
                  }}
                >
                  {scriptFile ? "변경" : "추가"}
                </Button>
                <Button
                  variant="contained"
                  onClick={() => setScriptFile(null)}
                  sx={{
                    backgroundColor: "#ee2727",
                    color: "#ffffff",
                    borderRadius: "25px",
                    marginLeft: "10px",
                    "&:hover": {
                      backgroundColor: "#ee2727",
                    },
                  }}
                >
                  삭제
                </Button>
              </>
            )}
            <input
              type="file"
              hidden
              ref={scriptFileInput}
              onChange={handleScriptFileChange}
              accept="text/*"
            />
          </div>
          {/* 음성 파일 첨부 */}
          <div
            style={{
              display: "flex",
              alignItems: "center",
              margin: "10px",
            }}
          >
            <MicIcon style={{ fontSize: 30 }} />
            {/* CustomAudioPlayer를 사용하신다면 이 부분에 추가하시면 됩니다. */}
            <a
              href={recordFileUrl}
              download
              style={{ flexGrow: 1, marginLeft: "10px" }}
            >
              {recordFile ? recordFile.name : ""}
            </a>
            {role !== "read" && (
              <>
                <Button
                  variant="contained"
                  onClick={() => recordFileInput.current?.click()}
                  sx={{
                    backgroundColor: "#5b5ff4",
                    color: "#ffffff",
                    borderRadius: "25px",
                    marginLeft: "10px",
                    "&:hover": {
                      backgroundColor: "#5b5ff4",
                    },
                  }}
                >
                  {recordFile ? "변경" : "추가"}
                </Button>
                <Button
                  variant="contained"
                  onClick={() => setRecordFile(null)}
                  sx={{
                    backgroundColor: "#ee2727",
                    color: "#ffffff",
                    borderRadius: "25px",
                    marginLeft: "10px",
                    "&:hover": {
                      backgroundColor: "#ee2727",
                    },
                  }}
                >
                  삭제
                </Button>
              </>
            )}
            <input
              type="file"
              hidden
              ref={recordFileInput}
              onChange={handleRecordFileChange}
              accept="audio/*"
            />
          </div>
          {/* 유튜브 링크 입력 */}
          <div
            style={{
              display: "flex",
              flexDirection: "row",
              justifyContent: "space-between",
              alignItems: "center",
              paddingBottom: "5px",
            }}
          >
            <YouTubeIcon style={{ fontSize: 30, marginLeft: "10px" }} />
            <TextField
              label="유튜브 링크"
              value={youtubeUrl}
              onChange={(e) => setYoutubeUrl(e.target.value)}
              disabled={role === "read"}
              style={{ flexGrow: 1, marginLeft: "20px", marginInline: "10px" }}
            />
          </div>
        </div>
      </DialogContent>
      {/* ------------------------------------------------------------ */}
      {/* ------------------------------------------------------------ */}
      <DialogActions>
        {role === "create" && (
          <Button onClick={() => confirmAndExecute(createWork)}>생성</Button>
        )}
        {role === "change" && (
          <>
            <Button onClick={() => confirmAndExecute(updateWork)}>
              수정 완료
            </Button>
            <Button onClick={() => confirmAndExecute(deleteWork)}>삭제</Button>
          </>
        )}
      </DialogActions>
    </Dialog>
  );
}
