import { Button } from '@mui/material';
import React, { useState, useRef, useEffect } from 'react';
import styled from 'styled-components';
interface AudioRecorderProps {
	getAudioURL: (url: string) => void;
}

const AudioStyleDiv = styled.div`
	.button-rec-start {
		display: flex;
		margin: auto;
	}

	.button-rec-end {
		display: flex;
		margin: auto;
	}

	.button-dl {
		display: flex;
		margin: auto 5px;
	}

	.button-delete {
		display: flex;
		margin: auto 5px;
	}

	.after-rec {
		display: flex;
		margin: auto;
	}
`;

const AudioRecorder: React.FC<AudioRecorderProps> = ({ getAudioURL }) => {
	const [recording, setRecording] = useState(false);
	const [audioURL, setAudioURL] = useState('');
	const audioRef = useRef<HTMLAudioElement | null>(null);
	const mediaRecorderRef = useRef<MediaRecorder | null>(null);

	useEffect(() => {
		getAudioURL(audioURL);
	}, [audioURL, getAudioURL]);

	const startRecording = () => {
		navigator.mediaDevices
			.getUserMedia({ audio: true })
			.then((stream) => {
				mediaRecorderRef.current = new MediaRecorder(stream);
				const audioChunks: Blob[] = [];

				mediaRecorderRef.current!.addEventListener('dataavailable', (event) => {
					audioChunks.push(event.data);
				});

				mediaRecorderRef.current!.addEventListener('stop', () => {
					const audioBlob = new Blob(audioChunks, { type: 'audio/wav' });
					setAudioURL(URL.createObjectURL(audioBlob));
					audioChunks.length = 0;
				});

				mediaRecorderRef.current!.start();
				setRecording(true);
			})
			.catch((error) => {
				console.error('음성 녹음을 시작하는 동안 오류가 발생했습니다:', error);
			});
	};

	const stopRecording = () => {
		if (mediaRecorderRef.current) {
			mediaRecorderRef.current.stop();
			setRecording(false);
		}
	};

	const deleteRecording = () => {
		setAudioURL('');
	};

	return (
		<AudioStyleDiv>
			{!audioURL ? (
				!recording ? (
					<Button
						className="button-rec-start"
						variant="outlined"
						color="error"
						size="large"
						onClick={startRecording}
					>
						● 녹음 시작
					</Button>
				) : (
					<Button
						className="button-rec-stop"
						variant="contained"
						color="error"
						size="large"
						onClick={stopRecording}
					>
						■ 녹음 정지
					</Button>
				)
			) : (
				<div className="after-rec">
					<audio className="audio" ref={audioRef} src={audioURL} controls />
					<a
						className="button-dl"
						href={audioURL}
						download="recorded_audio.wav"
					>
						<Button
							className="button-dl"
							color="secondary"
							variant="contained"
							size="large"
							onClick={stopRecording}
						>
							음성 다운로드
						</Button>
					</a>
					<Button
						className="button-delete"
						color="warning"
						variant="contained"
						size="large"
						onClick={deleteRecording}
					>
						음성 삭제
					</Button>
				</div>
			)}
		</AudioStyleDiv>
	);
};

export default AudioRecorder;
