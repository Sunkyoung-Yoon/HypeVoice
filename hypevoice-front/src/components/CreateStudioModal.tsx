import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Grid,
  FormHelperText,
  IconButton,
  Box,
  Typography,
} from "@mui/material";
import {
  Lock,
  LockOpen,
  ArrowDropUp,
  ArrowDropDown,
} from "@mui/icons-material";
import styled from "styled-components";
import { useState } from "react";
import { axiosClient } from "@/api/axios";
import { MakeStudioData, StudioResponse } from "./type";
import { LoginState, StudioId } from "@/recoil/Auth";
import { useRecoilValue, useRecoilState } from "recoil";
import { getCookie } from "../api/cookie";

const CreateButton = styled(Button)`
  position: absolute;
  right: 20px;
  bottom: 20px;
`;

export default function CreateStudioModal() {
  const [title, setTitle] = useState("");
  const [intro, setIntro] = useState("");
  const [password, setPassword] = useState<number | null>(null);
  const [open, setOpen] = useState(false);
  const [isPublic, setIsPublic] = useState(true);
  const [people, setPeople] = useState(1);
  const loginState = useRecoilValue(LoginState);
  const [studioId, setStudioId] = useRecoilState(StudioId);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleCheck = () => {
    setIsPublic(!isPublic);
    if (!isPublic) {
      // 비밀방으로 변경하는 경우 비밀번호를 초기화
      setPassword(null);
    }
  };

  const handleClick = async () => {
    if (isValidInput()) {
      await createStudios(inputData);
      handleClose();
    } else {
      alert("입력값을 확인해주세요");
    }
  };

  const inputData: MakeStudioData = {
    title: title,
    intro: intro,
    limitNumber: people,
    isPublic: isPublic ? 1 : 0,
    password: password,
  };

  // 사용자가 방을 만들기 위해 넣은 값들이 유효한 지 체크
  // 제목은 무조건 있어야하고 // 공개방이면 null이어야 하고 비밀방이면 4자리의 숫자여야 함
  const isValidInput = () => {
    const passwordIsValid = isPublic
      ? password === null
      : password !== null && /^\d{4}$/.test(password.toString());
    return title !== "" && passwordIsValid;
  };

  const createStudios = async (
    inputData: MakeStudioData
  ): Promise<StudioResponse | null | undefined> => {
    const accessToken = getCookie("access_token");
    console.log("만들려는 방의 정보는");
    console.log(inputData);
    console.log("방 만들기 위해 집어넣는 토큰은");
    console.log(accessToken);
    try {
      const response = await axiosClient.post<StudioResponse>(
        "/api/studios",
        inputData,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
        }
      );

      if (response?.data?.studioId !== null) {
        console.log(response);
        console.log(response?.data);
        setStudioId(response.data);
        alert(`방 생성 성공! ${response.data}번 방으로 이동합니다!`);
      } else {
        alert("방 생성 실패");
      }
      handleClose();
      return response.data;
    } catch (error) {
      handleClose();
      console.log(error);
      return null;
    }
  };

  const handleIncrease = () => {
    if (people < 6) setPeople(people + 1);
  };

  const handleDecrease = () => {
    if (people > 1) setPeople(people - 1);
  };

  return (
    <div>
      <CreateButton variant="contained" color="primary" onClick={handleOpen}>
        방 만들기
      </CreateButton>
      <Dialog open={open} onClose={handleClose} fullWidth maxWidth="sm">
        <DialogTitle>방 만들기</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="방 제목"
            type="text"
            fullWidth
            inputProps={{ maxLength: 20 }}
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
          <FormHelperText>최대 20자</FormHelperText>
          <TextField
            margin="dense"
            label="방 소개"
            type="text"
            fullWidth
            multiline
            rows={4}
            inputProps={{ maxLength: 50 }}
            value={intro}
            onChange={(e) => setIntro(e.target.value)}
          />
          <FormHelperText>최대 50자</FormHelperText>
          <Box mt={2}>
            <Grid container spacing={2}>
              <Grid item xs={4}>
                <TextField
                  label="인원"
                  type="number"
                  InputLabelProps={{
                    shrink: true,
                  }}
                  variant="standard"
                  inputProps={{
                    min: 1,
                    max: 6,
                  }}
                  value={people}
                  onChange={(e) => {
                    const input = parseInt(e.target.value);
                    if (isNaN(input) || input < 1 || input > 6) {
                      alert("1~6 사이의 숫자만 가능합니다!!");
                    } else {
                      setPeople(input);
                    }
                  }}
                />
                <IconButton
                  onClick={handleIncrease}
                  size="small"
                  disabled={people >= 6}
                >
                  <ArrowDropUp />
                </IconButton>
                <IconButton
                  onClick={handleDecrease}
                  size="small"
                  disabled={people <= 1}
                >
                  <ArrowDropDown />
                </IconButton>
                <FormHelperText>최대 6명</FormHelperText>
              </Grid>
              <Grid
                item
                xs={4}
                style={{
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "flex-end",
                }}
              >
                <IconButton
                  onClick={handleCheck}
                  color={isPublic ? "default" : "primary"}
                  style={{ marginRight: "10px" }}
                >
                  {isPublic ? <LockOpen /> : <Lock />}
                </IconButton>
                <Typography variant="caption" display="block" gutterBottom>
                  {isPublic ? "공개방" : "비밀방"}
                </Typography>
              </Grid>
              <Grid item xs={4}>
                <TextField
                  margin="dense"
                  label="비밀번호"
                  type="text"
                  fullWidth
                  disabled={isPublic}
                  inputProps={{ maxLength: 4 }}
                  value={password}
                  onChange={(e) => {
                    const input = e.target.value;
                    const isNumeric = /^\d+$/.test(input); // 숫자 외의 값이 들어오면 예외처리

                    if (isNumeric) {
                      setPassword(parseInt(input));
                    } else {
                      alert("4자리의 숫자만 입력 가능합니다!");
                    }
                  }}
                />
                <FormHelperText>4자리 숫자만 가능</FormHelperText>
              </Grid>
            </Grid>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>취소</Button>
          {loginState && (
            <Button
              onClick={handleClick}
              disabled={!isValidInput()}
              style={{ backgroundColor: isValidInput() ? "white" : "#D3D3D3" }}
            >
              생성
            </Button>
          )}
        </DialogActions>
      </Dialog>
    </div>
  );
}
