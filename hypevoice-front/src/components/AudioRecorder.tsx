import React, { useState, useRef } from 'react';

const AudioRecorder: React.FC = () => {
	const [recording, setRecording] = useState(false);
	const [audioURL, setAudioURL] = useState('');
	const audioRef = useRef<HTMLAudioElement | null>(null);
	const mediaRecorderRef = useRef<MediaRecorder | null>(null);

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
					const url = URL.createObjectURL(audioBlob);
					setAudioURL(url);
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

	const playAudio = () => {
		if (audioRef.current) {
			audioRef.current.play();
		}
	};

	return (
		<div>
			{!recording ? (
				<button onClick={startRecording}>녹음 시작</button>
			) : (
				<button onClick={stopRecording}>녹음 정지</button>
			)}
			{audioURL && (
				<div>
					<button onClick={playAudio}>음성 재생</button>
					<audio ref={audioRef} src={audioURL} controls />
					<a href={audioURL} download="recorded_audio.wav">
						<button>음성 다운로드</button>
					</a>
				</div>
			)}
		</div>
	);
};

export default AudioRecorder;
