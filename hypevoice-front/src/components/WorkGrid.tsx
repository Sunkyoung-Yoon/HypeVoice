// import React from "react";
import Container from "@mui/material/Container";
import Grid from "@mui/material/Grid";
import Work from "./Work";

export type WorkData = {
  workId: number;
  title: string;
  videoLink: string;
  photoUrl: string;
  scriptUrl: string;
  recordUrl: string;
  info: string;
  isRep: number;
};

const WorkDatas: WorkData[] = [
  {
    workId: 1,
    title: "제목1제목1제목1",
    videoLink: "https://www.youtube.com",
    photoUrl: "https://cdn.pixabay.com/photo/2023/08/04/11/47/anime-8169104_1280.jpg",
    scriptUrl: "https://www.naver.com",
    recordUrl: "",
    info: "설명1설명1설명1설명1설명1설명1설명1설명1설명1",
    isRep: 1,
  },
  {
    workId: 2,
    title: "제목2제목2제목2",
    videoLink: "",
    photoUrl: "https://cdn.pixabay.com/photo/2022/12/01/04/35/sunset-7628294_1280.jpg",
    scriptUrl: "",
    recordUrl: "",
    info: "설명2설명2설명2설명2설명2설명2설명2설명2설명2",
    isRep: 0,
  },
  {
    workId: 3,
    title: "제목3제목3제목3",
    videoLink: "", 
    photoUrl: "https://cdn.pixabay.com/photo/2020/11/15/18/31/cat-5746771_1280.png",
    scriptUrl: "",
    recordUrl: "",
    info: "설명3설명3설명3설명3설명3설명3설명3설명3설명3",
    isRep: 0,
  },
  {
    workId: 4,
    title: "제목4제목4제목4",
    videoLink: "",
    photoUrl: "https://source.unsplash.com/random?wallpapers",
    scriptUrl: "",
    recordUrl: "",
    info: "설명4설명4설명4설명4설명4설명4설명4설명4설명4",
    isRep: 0,
  },
  {
    workId: 5,
    title: "제목5제목5제목5",
    videoLink: "",
    photoUrl: "https://source.unsplash.com/random?wallpapers",
    scriptUrl: "",
    recordUrl: "",
    info: "설명5설명5설명5설명5설명5설명5설명5설명5설명5",
    isRep: 0,
  },
  {
    workId: 6,
    title: "제목6제목6제목6",
    videoLink: "",
    photoUrl: "https://source.unsplash.com/random?wallpapers",
    scriptUrl: "",
    recordUrl: "",
    info: "설명6설명6설명6설명6설명6설명6설명6설명6설명6",
    isRep: 0,
  },
  {
    workId: 7,
    title: "제목7제목7제목7",
    videoLink: "",
    photoUrl: "https://source.unsplash.com/random?wallpapers",
    scriptUrl: "",
    recordUrl: "",
    info: "설명7설명7설명7설명7설명7설명7설명7설명7설명7",
    isRep: 0,
  },
  {
    workId: 8,
    title: "제목8제목8제목8",
    videoLink: "",
    photoUrl: "https://source.unsplash.com/random?wallpapers",
    scriptUrl: "",
    recordUrl: "",
    info: "설명8설명8설명8설명8설명8설명8설명8설명8설명8",
    isRep: 0,
  },
  {
    workId: 9,
    title: "제목9제목9제목9",
    videoLink: "",
    photoUrl: "https://source.unsplash.com/random?wallpapers",
    scriptUrl: "",
    recordUrl: "",
    info: "설명9설명9설명9설명9설명9설명9설명9설명9설명9",
    isRep: 0,
  },
];

function WorkGrid() {
  return (
    <>
      <Container sx={{ py: 8 }} maxWidth="md">
        <Grid container spacing={4}>
          {WorkDatas.map((work) => (
            <Work work={work} />
          ))}
        </Grid>
      </Container>
    </>
  );
}

export default WorkGrid;
