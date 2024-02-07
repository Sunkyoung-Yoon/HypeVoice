// import React from "react";
import AddInfo from "./AddInfo";
import InlineHeader from "./InlineHeader";
import MyInfo from "./MyInfo";
import WorkGrid from "./WorkGrid";
import ScrollNavigation from "./ScrollNavigation";

function Voice() {
  return (
    <>
      <ScrollNavigation />
      <section id="top">
        <MyInfo />
      </section>
      <section id="work">
        <InlineHeader title={"💾 작업물"} worksCnt={9} storageSpace={17} />
        <WorkGrid />
      </section>
      <section id="addInfo">
        <InlineHeader title={"🔎 추가 정보"} worksCnt={0} storageSpace={0} />
        <AddInfo />
        <section id="bottom"></section>
      </section>
    </>
  );
}

export default Voice;
