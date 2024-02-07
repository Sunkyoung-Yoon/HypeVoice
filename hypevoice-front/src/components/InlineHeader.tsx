import styled from "styled-components";

type OwnProps = {
  title: string;
  worksCnt?: number;
  storageSpace?: number;
};

const Hr = styled.hr`
  height: 5px;
  width: 100%;
  border: none;
  background-color: black;
  margin: 3px 5px 3px 5px;
`;
// worksCnt로 1 이상의 number가 전달되면 작업물 개수가 표시되고, storageSpace로 1 이상의 number가 전달되면 용량이 MB 단위로 표시됨
// 필요한 곳에서 <InlineHeader title={"인라인헤더 제목"} worksCnt={0} storageSpace={0} /> 형태로 사용
function InlineHeader(props: OwnProps) {
  return (
    <div style={{margin: "10px"}}>
      <div style={{ margin: "0px 5px 0px 5px", display: "flex", alignItems: "flex-end", justifyContent: "space-between" }}>
        <div style={{ justifyContent: "left", fontSize: "30px" }}>{props.title}</div>
        <div style={{ display: "flex", justifyContent: "right", fontSize: "16px" }}>
          {props.worksCnt > 0 && <div style={{marginRight: "5px"}}>작업물:{props.worksCnt}개</div>}
          {props.storageSpace > 0 && <div>저장 공간:{ props.storageSpace }MB</div>}
        </div>
      </div>
      <Hr />
    </div>
  );
}
export default InlineHeader;

import styled from 'styled-components';

type OwnProps = {
  title: string,
  worksCnt: number,
  storageSpace : number
}

const Hr = styled.hr`
  height: 5px;
  border : none;
  background-color: black;
  margin: 3px 5px 3px 5px;
`;

// worksCnt로 1 이상의 number가 전달되면 작업물 개수가 표시되고, storageSpace로 1 이상의 number가 전달되면 용량이 MB 단위로 표시됨
// 필요한 곳에서 <InlineHeader title={"인라인헤더 제목"} worksCnt={0} storageSpace={0} /> 형태로 사용
function InlineHeader( props:OwnProps ) {
  return (
    <div style={{margin: "10px"}}>
      <div style={{ margin: "0px 5px 0px 5px", display: "flex", alignItems: "flex-end", justifyContent: "space-between" }}>
        <div style={{ justifyContent: "left", fontSize: "22px" }}>{props.title}</div>
        <div style={{ display: "flex", justifyContent: "right", fontSize: "16px" }}>
          {props.worksCnt > 0 && <div style={{marginRight: "5px"}}>작업물:{props.worksCnt}개</div>}
          {props.storageSpace > 0 && <div>저장 공간:{ props.storageSpace }MB</div>}
        </div>
      </div>
      <Hr />
    </div>
  );
}

export default InlineHeader;
