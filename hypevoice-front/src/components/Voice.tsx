import React, { useState } from "react";
import AddInfo from "./AddInfo";
import InlineHeader from "./InlineHeader";
import MyInfo from "./MyInfo";
import WorkGrid from "./WorkGrid";

function Voice() {
  const [workCount, setWorkCount] = useState(0);
  return (
    <>
      {/* <ScrollNavigation /> */}
      <section id="top" style={{ marginBottom: "10px" }}>
        <MyInfo />
      </section>
      <section id="work">
        {/* <SearchComponent
          searchBarStateAtom={VoiceCurrentKeyword}
          filterAtom={VoiceCurrentFilterAtom}
        /> */}
        <InlineHeader
          title={"💾 작업물"}
          worksCnt={workCount}
          storageSpace={17}
        />
        <WorkGrid setWorkCount={setWorkCount} />
      </section>
      <section id="addInfo">
        <InlineHeader title={"📋 추가 정보"} />
        <AddInfo />
        <section id="bottom"></section>
      </section>
    </>
  );
}

export default Voice;
