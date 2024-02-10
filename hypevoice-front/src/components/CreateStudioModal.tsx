import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  InputAdornment,
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

const CreateButton = styled(Button)`
  position: absolute;
  right: 20px;
  bottom: 20px;
`;

export default function CreateStudioModal() {
  const [open, setOpen] = useState(false);
  const [isPublic, setIsPublic] = useState(true);
  const [people, setPeople] = useState(1);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);
  const handleCheck = () => setIsPublic(!isPublic);

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
                  onChange={(e) => setPeople(e.target.value)}
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
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">#</InputAdornment>
                    ),
                  }}
                />
                <FormHelperText>4자리 숫자만 가능</FormHelperText>
              </Grid>
            </Grid>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>취소</Button>
          <Button onClick={handleClose}>생성</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}