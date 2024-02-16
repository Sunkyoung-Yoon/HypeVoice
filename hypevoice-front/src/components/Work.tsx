import { useState } from "react";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import CardMedia from "@mui/material/CardMedia";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import { Chip } from "@mui/material";
import styled from "styled-components";
import { WorkData } from "./WorkGrid";
import StarIcon from "@mui/icons-material/Star";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";

const CategoryTagesWrapper = styled.div`
  display: flex;
  justify-content: space-around;
`;

const Category = styled(Chip)`
  width: 80px;
`;

const modalStyle = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 700,
  height: 300,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

interface OwnProps {
  work: WorkData;
}

function Work(props: OwnProps) {
  const [star, setStar] = useState(props.work.isRep);

  const [workOpen, setWorkOpen] = useState(false);
  const handleWorkOpen = () => setWorkOpen(true);
  const handleWorkClose = () => setWorkOpen(false);

  const [scriptOpen, setScriptOpen] = useState(false);
  const handleScriptOpen = () => setScriptOpen(true);
  const handleScriptClose = () => setScriptOpen(false);

  const [videoOpen, setvideoOpen] = useState(false);
  const handleVideoOpen = () => setvideoOpen(true);
  const handleVideoClose = () => setvideoOpen(false);

  return (
    <>
      <Grid item key={props.work.workId} xs={12} sm={6} md={4}>
        <Card
          style={{ position: "relative" }}
          sx={{ height: "100%", display: "flex", flexDirection: "column" }}
        >
          <div className="cardButton">
            {star == 1 && (
              <StarIcon
                style={{
                  padding: "0px 3px 3px 3px",
                  position: "absolute",
                  top: "3px",
                  left: "0px",
                  color: "Gold",
                  fontSize: "30px",
                }}
              />
            )}
            <button
              style={{
                padding: "3px",
                position: "absolute",
                top: "3px",
                left: "180px",
                color: "white",
                backgroundColor: "transparent",
                textShadow:
                  "-1px 0 black, 0 1px black, 1px 0 black, 0 -1px black",
              }}
            >
              수정
            </button>
            <button
              style={{
                padding: "3px",
                position: "absolute",
                top: "3px",
                left: "220px",
                color: "white",
                backgroundColor: "transparent",
                textShadow:
                  "-1px 0 black, 0 1px black, 1px 0 black, 0 -1px black",
              }}
            >
              삭제
            </button>
          </div>
          <CardMedia
            component="div"
            sx={{
              // 16:9
              pt: "56.25%",
            }}
            image={props.work.photoUrl}
            onClick={handleWorkOpen}
          />
          <Modal
            open={workOpen}
            onClose={handleWorkClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
          >
            <Box sx={modalStyle}>
              {/* <img src={props.work.photoUrl} alt="작업물 이미지" title={props.work.title} /> */}
              <div
                className="workViewImg"
                style={{
                  width: "200px",
                  height: "112.5px",
                  background: `url(${props.work.photoUrl}) no-repeat center/cover`,
                }}
              />
              <Typography id="modal-modal-title" variant="h6" component="h2">
                {props.work.title}
              </Typography>
              <Typography id="modal-modal-description" sx={{ mt: 2 }}>
                작업물 모달창
              </Typography>
              <a href={props.work.videoLink} target="_blank" rel="noreferrer">
                {props.work.videoLink}
              </a>
              <button onClick={handleScriptOpen}>{props.work.scriptUrl}</button>
              <button
                style={{ position: "absolute", top: 10, right: 10 }}
                onClick={handleWorkClose}
              >
                X
              </button>
            </Box>
          </Modal>
          <CategoryTagesWrapper style={{ padding: "3px" }}>
            <Category label="오디오드라마" size="small" variant="outlined" />
            <Category label="저음" size="small" variant="outlined" />
            <Category label="따뜻한" size="small" variant="outlined" />
            <Category label="남성" size="small" variant="outlined" />
            <Category label="청년" size="small" variant="outlined" />
          </CategoryTagesWrapper>
          <CardContent style={{ paddingBottom: "0px" }} sx={{ flexGrow: 1 }}>
            <Typography
              gutterBottom
              variant="h5"
              component="h2"
              align="left"
              onClick={handleWorkOpen}
            >
              {props.work.title}
            </Typography>
            <Typography align="left">{props.work.info}</Typography>
          </CardContent>
        </Card>
      </Grid>
    </>
  );
}

export default Work;
