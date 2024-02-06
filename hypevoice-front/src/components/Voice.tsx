// import React from "react";
import Experience from "./Experience";
import InlineHeader from "./InlineHeader";
import MyInfo from "./MyInfo";
import WorkGrid from "./WorkGrid";

function Voice() {
  return (
    <>
      <MyInfo />
      <InlineHeader title={"작업물"} worksCnt={9} storageSpace={17} />
      <WorkGrid />
      <InlineHeader title={"이력"} worksCnt={0} storageSpace={0} />
      <Experience />
    </>
  )
}

export default Voice;