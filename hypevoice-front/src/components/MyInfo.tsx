import { useEffect, useState } from "react";
import { styled } from "@mui/material/styles";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import ButtonBase from "@mui/material/ButtonBase";
import FavoriteIcon from "@mui/icons-material/Favorite";
import { axiosClient } from "@/api/axios";
import { useRecoilValue } from "recoil";
import { MyInfoVoiceId } from "@/recoil/CurrentVoiceId/MyInfoVoiceId";

const Img = styled("img")({
  margin: "auto",
  display: "block",
  maxWidth: "100%",
  maxHeight: "100%",
});

type VoiceDataType = {
  name: string,
  imageUrl: string,
  intro: string,
  email: string,
  phone: string,
  addInfo: string,
  likes: number
}

const getVoiceData = async (voiceId : number): Promise<VoiceDataType> => {
  const response = await axiosClient.get(`/api/voices/${voiceId}`);
  console.log(response.data);
  return response.data;
};

function updateLike() {
  alert("좋아요 수정");
}

function MyInfo() {
  const currentVoiceId = useRecoilValue(MyInfoVoiceId);
  const [favoriteCnt, setFavoriteCnt] = useState(0);
  const [currentVoice, setCurrentVoice] = useState<VoiceDataType | null>(null);

  useEffect(() => {
    const fetchVoiceData = async () => {
      try {
        const voiceData = await getVoiceData(currentVoiceId);
        setCurrentVoice(voiceData);
        setFavoriteCnt(voiceData.likes);
      } catch (error) {
        console.error(error);
      }
    };
    
    fetchVoiceData();
  });

  return (
    <>
      <Paper
        sx={{
          p: 2,
          margin: "auto",
          maxWidth: 500,
          flexGrow: 1,
          backgroundColor: (theme) => (theme.palette.mode === "dark" ? "#1A2027" : "#fff"),
        }}
      >
        <Grid container spacing={2}>
          <Grid item>
            <ButtonBase sx={{ width: 128, height: 128 }}>
              <Img alt="profileImg" src={currentVoice?.imageUrl} />
            </ButtonBase>
          </Grid>
          <Grid item xs={12} sm container>
            <Grid item xs container direction="column" spacing={2}>
              <Grid item xs>
                <Typography gutterBottom variant="h4" component="div">
                  {currentVoice?.name}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Email : {currentVoice?.email}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Call : {currentVoice?.phone}
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  {currentVoice?.addInfo}
                </Typography>
              </Grid>
              <></>
            </Grid>
            <Grid item>
              <FavoriteIcon color="error" fontSize="large" onClick={() => {
                updateLike();
              }} />
              <span>{favoriteCnt}</span>
            </Grid>
          </Grid>
        </Grid>
        <Typography variant="body2" sx={{ textAlign: "center", marginTop: "1rem" }}>
          {currentVoice?.intro}
        </Typography>
      </Paper>
    </>
  );
}

export default MyInfo;