function AddInfo({ info } : { info: string }) {
  return (
    <div style={{margin: "0px 50px", wordBreak:"break-all"}}>
      {info}
    </div>
  );
};

export default AddInfo;