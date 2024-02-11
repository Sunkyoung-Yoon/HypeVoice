import React, { useState, useRef } from 'react';

const AudioRecorder: React.FC = () => {
	const [recording, setRecording] = useState(false);
	const [audioURL, setAudioURL] = useState('');
	const audioRef = useRef<HTMLAudioElement | null>(null);

	const startRecording = () => {
		navigator.mediaDevices
			.getUserMedia({ audio: true })
			.then((stream) => {
				const mediaRecorder = new MediaRecorder(stream);
				const audioChunks: Blob[] = [];

				mediaRecorder.addEventListener('dataavailable', (event) => {
					audioChunks.push(event.data);
				});

				mediaRecorder.addEventListener('stop', () => {
					const audioBlob = new Blob(audioChunks);
					const url = URL.createObjectURL(audioBlob);
					setAudioURL(url);
					// uploadAudio(audioBlob); // 음성 파일을 서버에 업로드
					audioChunks.length = 0;
				});

				mediaRecorder.start();
				setRecording(true);

				setTimeout(() => {
					mediaRecorder.stop();
					setRecording(false);
				}, 10000); // 5초간 녹음 후 자동으로 정지
			})
			.catch((error) => {
				console.error('음성 녹음을 시작하는 동안 오류가 발생했습니다:', error);
			});
	};

	const playAudio = () => {
		if (audioRef.current) {
			audioRef.current.play();
		}
	};

	// const uploadAudio = (audioBlob: Blob) => {
	// 	// 파일 업로드를 위한 서버 요청 코드 작성
	// 	// 예를 들어, axios 라이브러리를 사용한 POST 요청:
	// 	axios
	// 		.post('/upload', audioBlob)
	// 		.then((response) => {
	// 			console.log('음성 파일 업로드 성공:', response.data);
	// 		})
	// 		.catch((error) => {
	// 			console.error('음성 파일 업로드 중 오류가 발생했습니다:', error);
	// 		});
	// };

	return (
		<div>
			<button onClick={startRecording} disabled={recording}>
				{recording ? '녹음 중...' : '녹음 시작'}
			</button>
			{audioURL && (
				<div>
					<button onClick={playAudio}>음성 재생</button>
					<audio ref={audioRef} src={audioURL} controls />
				</div>
			)}
		</div>
	);
};

export default AudioRecorder;
