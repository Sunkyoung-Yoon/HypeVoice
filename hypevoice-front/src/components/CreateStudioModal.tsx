// CreateStudioModal.tsx
import React, { useState } from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
} from "@mui/material";
import styled from "styled-components";

// 스타일 컴포넌트를 정의합니다.
const CreateButton = styled(Button)`
  position: absolute;
  right: 20px;
  bottom: 20px;
`;

export default function CreateStudioModal() {
  const [open, setOpen] = useState(false);

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  return (
    <div>
      <CreateButton variant="contained" color="primary" onClick={handleOpen}>
        스튜디오 생성
      </CreateButton>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>스튜디오 생성</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="스튜디오명"
            type="text"
            fullWidth
          />
          <TextField
            margin="dense"
            label="스튜디오 소개"
            type="text"
            fullWidth
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>취소</Button>
          <Button onClick={handleClose}>생성</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
