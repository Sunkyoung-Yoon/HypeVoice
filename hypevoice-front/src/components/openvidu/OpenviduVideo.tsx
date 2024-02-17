import React, { useRef, useEffect } from 'react';
import { StreamManager } from 'openvidu-browser';
import { Button } from '@mui/material';

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
			<video autoPlay={autoplay} ref={videoRef} style={{ width: '100%' }}>
				<track kind="captions" />
			</video>
			<Button onClick={() => audioMuteHandler()}>AudioMute</Button>
			<Button onClick={() => videoMuteHandler()}>VideoMute</Button>
		</>
	);
import React, { useRef, useEffect } from "react";
import { StreamManager } from "openvidu-browser";
import { Button } from "@mui/material";
import DesktopAccessDisabledIcon from "@mui/icons-material/DesktopAccessDisabled";
import VolumeOffIcon from "@mui/icons-material/VolumeOff";

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
      <video
        autoPlay={autoplay}
        ref={videoRef}
        style={{ width: "100%", borderRadius: "15px", width: "100%" }}
      >
        <track kind="captions" />
      </video>
      <div
        style={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "flex-end",
          marginInline: "15px",
        }}
      >
        <button
          onClick={() => audioMuteHandler()}
          style={{ borderRadius: "45px", margin: "10px 5px" }}
        >
          <VolumeOffIcon />
        </button>
        <button
          onClick={() => videoMuteHandler()}
          style={{ borderRadius: "45px", margin: "10px 5px" }}
        >
          <DesktopAccessDisabledIcon />
        </button>
      </div>
    </>
  );
}

export default OpenviduVideo;
