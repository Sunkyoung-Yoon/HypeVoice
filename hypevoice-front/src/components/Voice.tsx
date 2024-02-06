// import React from "react";
import AddInfo from "./AddInfo";
import InlineHeader from "./InlineHeader";
import MyInfo from "./MyInfo";
import WorkGrid from "./WorkGrid";

function Voice() {
  return (
    <>
      <MyInfo />
      <InlineHeader title={"작업물"} worksCnt={9} storageSpace={17} />
      <WorkGrid />
      <InlineHeader title={"추가 정보"} worksCnt={0} storageSpace={0} />
      <AddInfo />
    </>
  )
}

export default Voice;