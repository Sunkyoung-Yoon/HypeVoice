import React, { useRef, useEffect } from "react";
import { StreamManager } from "openvidu-browser";
import { Button } from "@mui/material";

interface Props {
  streamManager: StreamManager;
  audioMuteHandler: () => void;
  videoMuteHandler: () => void;
}

function OpenviduVideo({
  streamManager,
  audioMuteHandler,
  videoMuteHandler,
}: Props) {
  const videoRef = useRef<HTMLVideoElement>(null);
  const autoplay = true;

  useEffect(() => {
    if (streamManager && videoRef.current) {
      streamManager.addVideoElement(videoRef.current);
    }
  }, [streamManager]);

  return (
    <>
      <video autoPlay={autoplay} ref={videoRef} style={{ width: "60%" }}>
        <track kind="captions" />
      </video>
      <Button onClick={() => audioMuteHandler()}>소리 켜기/끄기</Button>
      <Button onClick={() => videoMuteHandler()}>화면 켜기/끄기</Button>
    </>
  );
}

export default OpenviduVideo;
