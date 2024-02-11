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
  Grid,
  IconButton,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import { useState, useRef, useEffect } from "react";
import axios from "axios";
import { WorkModalProps } from "./type";
import { categories } from "./Category";

export default function WorkModal({
  open, // 모달창 열림
  onClose, // 모달창 닫힘
  role, // 모달 창 역할
  voiceId, // 보이스 아이디
  workId, // 작업물 아이디
}: WorkModalProps) {
  const [title, setTitle] = useState(""); // 제목 상태 관리
  const [youtubeUrl, setYoutubeUrl] = useState(""); // 유튜브 링크 상태 관리
  const [intro, setIntro] = useState(""); // 작업물 소개 상태 관리
  const [category, setCategory] = useState(""); // 고른 카테고리 상태 관리
  const [scriptFile, setScriptFile] = useState<File | null>(null); // 대본 파일 상태 관리
  const [recordFile, setRecordFile] = useState<File | null>(null); // 음성 파일 상태 관리
  const [imageFile, setImageFile] = useState<File | null>(null); // 이미지 파일 상태 관리
  const [preview, setPreview] = useState<string | null>(null); // 이미지 미리보기를 위한 상태 관리
  const scriptFileInput = useRef<HTMLInputElement | null>(null); // 입력한 대본 파일 상태 관리
  const recordFileInput = useRef<HTMLInputElement | null>(null); // 녹음한 음성 파일 상태 관리
  const imageFileInput = useRef<HTMLInputElement | null>(null); // 추가한 사진 파일 상태 관리

  // 대본 파일 수정
  const handleScriptFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setScriptFile(e.target.files ? e.target.files[0] : null);
  };

  // 음성 파일 수정
  const handleRecordFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setRecordFile(e.target.files ? e.target.files[0] : null);
  };

  // 사진 파일 수정 및 preview 설정
  const handleImageFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setImageFile(e.target.files ? e.target.files[0] : null);

    if (e.target.files && e.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setPreview(e.target ? (e.target.result as string) : null);
      };
      reader.readAsDataURL(e.target.files[0]);
    } else {
      setPreview(null);
    }
  };

  // 사진 삭제 및 preview 초기화
  const handleImageFileRemove = () => {
    // 이 함수를 통해 이미지 파일을 제거합니다.
    setImageFile(null);
    setPreview(null);
  };

  const createWork = async () => {
    const formData = new FormData();
    formData.append("title", title);
    formData.append("intro", intro);
    formData.append("youtubeUrl", youtubeUrl);
    formData.append("category", category);
    if (scriptFile) {
      formData.append("scriptFile", scriptFile);
    }
    if (recordFile) {
      formData.append("recordFile", recordFile);
    }
    if (imageFile) {
      formData.append("imageFile", imageFile);
    }

    try {
      const response = await axios.post(
        `/api/voices/${voiceId}/works`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );
      return response.data;
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  const updateWork = async () => {
    // 수정 버튼 클릭 시 실행할 로직 작성
  };

  const deleteWork = async () => {
    // 삭제 버튼 클릭 시 실행할 로직 작성
  };

  const confirmAndExecute = async (action: () => Promise<void>) => {
    if (window.confirm("정말 실행하시겠습니까?")) {
      await action();
    }
  };

  useEffect(() => {
    if (role === "change" || role === "read") {
      // workId를 이용하여 작업물 정보를 가져와 상태값 설정
    }
  }, [role, workId]);

  return (
    <Dialog open={open} onClose={onClose} maxWidth="xl" fullWidth={true}>
      <DialogTitle style={{ display: "flex", justifyContent: "flex-end" }}>
        {" "}
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
      {/* ------------------------------------------------------------ */}
      {/* ------------------------------------------------------------ */}
      {/* ------------------------------------------------------------ */}
      <DialogContent
        style={{
          display: "flex",
          flexDirection: "column",
          height: "80%",
        }}
      >
        <div style={{ display: "flex", height: "100%" }}>
          <div style={{ flex: 0.8, marginRight: "8px" }}>
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
                height: "70%", // 버튼의 높이를 설정합니다.
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
              {!preview && "사진 추가"} {/* 사진이 없으면 "사진 추가"*/}
              <input
                type="file"
                hidden
                ref={imageFileInput}
                onChange={handleImageFileChange}
                accept="image/*"
              />
            </Button>
            {/* 사진 삭제 버튼 */}
            {preview && ( // 이미지가 있을 때만 삭제 버튼을 보여줍니다.
              <Button
                onClick={handleImageFileRemove}
                style={{
                  marginLeft: "10px",
                  backgroundColor: "transparent",
                  color: "#5b5ff4",
                  padding: "5px 10px",
                }} // 버튼의 스타일을 변경합니다.
              >
                사진 삭제
              </Button>
            )}
            {/* 제목 입력 */}
            <TextField
              label="제목"
              variant="outlined"
              fullWidth
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              disabled={role === "read"}
              style={{ marginBottom: "8px" }}
            />
          </div>
          {/* ------------------------------------------------------------ */}
          {/* ------------------------------------------------------------ */}
          {/* ------------------------------------------------------------ */}
          <div style={{ flex: 2, display: "flex", flexDirection: "column" }}>
            {/* 카테고리 부분 */}
            <div style={{ display: "flex", flexWrap: "wrap" }}>
              {Object.keys(categories).map(
                (
                  category // 각 카테고리별로 `Select` 컴포넌트를 생성합니다.
                ) => (
                  <FormControl
                    variant="outlined"
                    style={{
                      marginBottom: "10px",
                      width: "18%",
                      marginRight: "1%",
                    }}
                  >
                    <InputLabel id={`${category}-label`}>{category}</InputLabel>
                    <Select
                      labelId={`${category}-label`}
                      id={category}
                      // value={values[category]}
                      // onChange={handleChange(category)}
                      label={category}
                    >
                      {categories[category].map(
                        (
                          option // 각 카테고리별로 선택 가능한 옵션을 생성합니다.
                        ) => (
                          <MenuItem value={option}>{option}</MenuItem>
                        )
                      )}
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
              rows={4}
              fullWidth
              value={intro}
              onChange={(e) => setIntro(e.target.value)}
              disabled={role === "read"}
              style={{ flexGrow: 1 }}
            />
          </div>
        </div>
        {/* ------------------------------------------------------------ */}
        {/* ------------------------------------------------------------ */}
        {/* ------------------------------------------------------------ */}
        {/* ------------------------------------------------------------ */}
        {/* ------------------------------------------------------------ */}
        <div
          style={{ display: "flex", flexDirection: "column", height: "20%" }}
        >
          {/* 대본 파일 첨부 */}
          <input
            type="file"
            hidden
            ref={scriptFileInput}
            onChange={handleScriptFileChange}
          />
          <Button
            variant="contained"
            onClick={() => scriptFileInput.current?.click()}
            disabled={role === "read"}
          >
            대본 파일 첨부
          </Button>
          {/* 음성 파일 첨부 */}
          <input
            type="file"
            hidden
            ref={recordFileInput}
            onChange={handleRecordFileChange}
          />
          <Button
            variant="contained"
            onClick={() => recordFileInput.current?.click()}
            disabled={role === "read"}
          >
            음성 파일 첨부
          </Button>
          {/* 유튜브 링크 입력 */}
          <TextField
            label="유튜브 링크"
            value={youtubeUrl}
            onChange={(e) => setYoutubeUrl(e.target.value)}
            disabled={role === "read"}
          />
        </div>
      </DialogContent>
      {/* ------------------------------------------------------------ */}
      {/* ------------------------------------------------------------ */}
      {/* ------------------------------------------------------------ */}
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
