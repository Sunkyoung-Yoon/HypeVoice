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
}

export default OpenviduVideo;
