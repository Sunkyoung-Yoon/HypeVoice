import { useState } from "react";
import { styled } from "@mui/material/styles";
import Grid from "@mui/material/Grid";
import Paper from "@mui/material/Paper";
import Typography from "@mui/material/Typography";
import ButtonBase from "@mui/material/ButtonBase";
import FavoriteIcon from "@mui/icons-material/Favorite";

const Img = styled("img")({
  margin: "auto",
  display: "block",
  maxWidth: "100%",
  maxHeight: "100%",
});

function MyInfo() {
  const [favoriteCnt, setFavoriteCnt] = useState(0);

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
              <Img alt="profileImg" src="src/assets/sunkyung.png" />
            </ButtonBase>
          </Grid>
          <Grid item xs={12} sm container>
            <Grid item xs container direction="column" spacing={2}>
              <Grid item xs>
                <Typography gutterBottom variant="h4" component="div">
                  윤선경
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Email : yskjdh@gmail.com
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Call : 010 - 4321 - 5678
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Youtube : yskjdh@youtube.com
                </Typography>
                <Typography variant="body1" color="text.secondary">
                  Instagram : yskjdh
                </Typography>
              </Grid>
              <></>
            </Grid>
            <Grid item>
              <FavoriteIcon color="error" fontSize="large" onClick={() => {
                setFavoriteCnt(favoriteCnt + 1);
              }} />
              <span>{favoriteCnt}</span>
            </Grid>
          </Grid>
          <Grid item>
            <Typography sx={{ cursor: "pointer" }} variant="body2">
              “ 성우는 기술자가 아니라 배우다 ”
              <br />
              맡은 배역 그 자체가 되어 목소리 뿐만 아니라 성격과 습관까지도 하나되는 연기를
              지향합니다.
            </Typography>
          </Grid>
        </Grid>
      </Paper>
    </>
  );
}

export default MyInfo;