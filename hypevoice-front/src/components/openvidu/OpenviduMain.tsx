import React from 'react';
import { useState, useEffect, useCallback } from 'react';
import {
	OpenVidu,
	Session as OVSession,
	Publisher,
	Subscriber,
} from 'openvidu-browser';
import axios from 'axios';
import OpenviduForm from './OpenviduForm';
import OpenviduSession from './OpenviduSession';
import { getCookie } from '../../api/cookie';
import { Box, Button, Modal, Typography } from '@mui/material';
const modalStyle = {
	position: 'absolute' as 'absolute',
	top: '50%',
	left: '50%',
	transform: 'translate(-50%, -50%)',
	width: 400,
	height: 400,
	bgcolor: 'background.paper',
	border: '2px solid #000',
	boxShadow: 24,
	p: 4,
};
type ChatType = {
	sender: string;
	message: string;
};
function OpenviduMain() {
	const OPENVIDU_SERVER_URL = `https://hypevoice.site`;
	const [session, setSession] = useState<OVSession | ''>('');
	const [sessionScreen, setSessionScreen] = useState<OVSession | ''>('');
	const [studioId, setStudioId] = useState<string>('');
	const [sessionId, setSessionId] = useState<string>('');
	const [subscriber, setSubscriber] = useState<Subscriber | null>(null);
	const [publisher, setPublisher] = useState<Publisher | null>(null);
	const [OV, setOV] = useState<OpenVidu | null>(null);
	const [screenOV, setScreenOV] = useState<OpenVidu | null>(null);
	const [order, setOrder] = useState<number>(0);
	const [newMessage, setNewMessage] = useState<string>('');
	const [recordingId, setRecordingId] = useState<string>('');
	const [messages, setMessages] = useState<ChatType[]>([]);
	const [isHost, setIsHost] = useState<boolean>(true);
	const [modalOpen, setModalOpen] = React.useState(false);
	const [recFiles, setRecFiles] = useState<string[]>([]);
	const handleRecModalOpen = async () => {
		setRecFiles(await getRecording());
		setModalOpen(true);
	};

	const handleRecModalClose = async () => {
		console.log(recFiles);
		await axios
			.delete(
				`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/${recordingId}`,
				{
					headers: { Authorization: `Bearer ${accessToken}` },
					data: { urls: recFiles },
				},
			)
			.catch((e) => console.log(e))
			.then(() => {
				setRecFiles([]);
			});
		setModalOpen(false);
	};

	// const OPENVIDU_SERVER_SECRET = 'MY_SECRET';
	const handleDelete = async (): Promise<void> => {
		try {
			const accessToken = getCookie('access_token');
			await axios.delete(`${OPENVIDU_SERVER_URL}/api/studios/${studioId}`, {
				headers: { Authorization: `Bearer ${accessToken}` },
			});
		} catch (error) {
			console.log(error);
		}
	};

	const accessToken = getCookie('access_token');

	const leaveSession = useCallback(() => {
		setOrder(0);
		if (session) session.disconnect();
		setOV(null);
		setScreenOV(null);
		setSession('');
		setSessionScreen('');
		setSessionId('');
		setSubscriber(null);
		setPublisher(null);
		setMessages([]);
		if (isHost) handleDelete();
	}, [session]);

	const CreateSession = () => {
		setOrder(1);
		const OVs = new OpenVidu();
		setOV(OVs);
		setSession(OVs.initSession());
		console.log('create');
	};

	const JoinSession = () => {
		setOrder(2);
		const OVs = new OpenVidu();
		setOV(OVs);
		setSession(OVs.initSession());
		console.log('join');
	};

	const shareScreen = () => {
		setOrder(3);
		const OVs = new OpenVidu();
		setScreenOV(OVs);
		setSessionScreen(OVs.initSession());
		console.log('share');
	};

	const recordingStart = async () => {
		console.log(studioId);
		const response = await axios
			.post(
				`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/start`,
				{},
				{
					params: { isIndividual: false },
					headers: {
						'Authorization': 'Bearer ' + accessToken,
					},
				},
			)
			.catch((e) => console.log(e));
		console.log(response.data);
		console.log('녹음시작');
		setRecordingId(response.data);
		return response.data;
	};

	const recordingStop = async () => {
		const response = await axios.post(
			`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/stop/${recordingId}`,
			{},
			{
				headers: {
					'Content-Type': 'application/json',
					'Authorization': 'Bearer ' + accessToken,
				},
			},
		);
		console.log('녹음완료 => 녹음결과 : ', response);
		return response;
	};

	const handleRecordDownload = () => {};

	const getRecording = async (): Promise<string[]> => {
		const response = await axios.get(
			`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/${recordingId}`,
			{
				headers: {
					'Content-Type': 'application/json',
					'Authorization': 'Bearer ' + accessToken,
				},
			},
		);
		console.log('받아온 결과 : ', response);
		return response.data;
	};

	const downloadFiles = async (fileUrl: string) => {
		try {
			// 각 파일 URL에 대해 Promise를 생성하여 배열에 저장합니다.

			const response = await axios({
				url: fileUrl,
				method: 'GET',
				responseType: 'blob', // Blob 형태로 응답 받습니다.
			});
			const blob = new Blob([response.data]);
			const url = window.URL.createObjectURL(blob);
			const filename = getFileNameFromUrl(fileUrl);

			// 다운로드 링크를 생성하고 클릭합니다.
			const a = document.createElement('a');
			a.href = url;
			a.download = filename; // 다운로드될 파일명 설정
			document.body.appendChild(a);
			a.click();

			// 사용이 끝난 URL 객체를 해제합니다.
			window.URL.revokeObjectURL(url);
		} catch (error) {
			console.error('Error downloading files:', error);
		}
	};

	// const downloadFiles = async (fileUrls: string) => {
	// 	try {
	// 		// 각 파일 URL에 대해 Promise를 생성하여 배열에 저장합니다.
	// 		const downloadPromises = fileUrls.map(async (fileUrl) => {
	// 			const response = await axios({
	// 				url: fileUrl,
	// 				method: 'GET',
	// 				responseType: 'blob', // Blob 형태로 응답 받습니다.
	// 			});
	// 			const blob = new Blob([response.data]);
	// 			const url = window.URL.createObjectURL(blob);
	// 			const filename = getFileNameFromUrl(fileUrl);

	// 			// 다운로드 링크를 생성하고 클릭합니다.
	// 			const a = document.createElement('a');
	// 			a.href = url;
	// 			a.download = filename; // 다운로드될 파일명 설정
	// 			document.body.appendChild(a);
	// 			a.click();

	// 			// 사용이 끝난 URL 객체를 해제합니다.
	// 			window.URL.revokeObjectURL(url);
	// 		});

	// 		// 모든 Promise가 완료될 때까지 대기합니다.
	// 		await Promise.all(downloadPromises);
	// 	} catch (error) {
	// 		console.error('Error downloading files:', error);
	// 	}
	// };

	const getFileNameFromUrl = (fileUrl: string) => {
		// 파일 URL에서 파일명을 추출합니다.
		const startIndex = fileUrl.lastIndexOf('/') + 1;
		const filename = fileUrl.substring(startIndex);
		return filename;
	};

	useEffect(() => {
		window.addEventListener('beforeunload', leaveSession);

		return () => {
			window.removeEventListener('beforeunload', leaveSession);
		};
	}, [leaveSession]);

	// const sessionIdChangeHandler = (
	// 	event: React.ChangeEvent<HTMLInputElement>,
	// ) => {
	// 	setSessionId(event.target.value);
	// };

	const studioIdHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
		setStudioId(event.target.value);
	};

	const sendMessage = () => {
		if (session === '') return;
		if (newMessage.trim() === '') return; // 빈 문자열인 경우 전송하지 않음

		session.signal({
			data: newMessage,
			to: [],
			type: 'chat',
		});
		setNewMessage('');
	};

	useEffect(() => {
		if (session === '') return;

		const handleChatSignal = (event) => {
			setMessages((messages) => [
				...messages,
				{
					sender: event.from.connectionId,
					message: event.data,
				},
			]);
		};

		session.on('signal:chat', handleChatSignal);

		return () => {
			session.off('signal:chat', handleChatSignal);
		};
	}, [session]);

	useEffect(() => {
		if (session === '') return;

		session.on('streamDestroyed', (event) => {
			if (subscriber && event.stream.streamId === subscriber.stream.streamId) {
				setSubscriber(null);
			}
		});
	}, [subscriber, session]);

	useEffect(() => {
		if (session != '') {
			session.on('streamCreated', (event) => {
				if (event.stream.typeOfVideo == 'CAMERA') {
					const subscribers = session.subscribe(
						event.stream,
						'container-cameras',
					);
					setSubscriber(subscribers);
				}
			});
		}

		if (sessionScreen != '') {
			sessionScreen.on('streamCreated', (event) => {
				if (event.stream.typeOfVideo == 'SCREEN') {
					const subscribers = sessionScreen.subscribe(
						event.stream,
						'container-screens',
					);
					setSubscriber(subscribers);
				}
			});
		}

		const createSession = async () => {
			const response = await axios.post(
				`${OPENVIDU_SERVER_URL}/api/studios`,
				{
					title: 'title',
					intro: 'intro',
					limitNumber: 6,
					isPublic: 1,
					passwrod: null,
				},
				{
					headers: {
						'Content-Type': 'application/json',
						'Authorization': 'Bearer ' + accessToken,
					},
				},
			);
			return response.data;
		};

		const createToken = async (studioId: string) => {
			console.log(studioId);
			const response = await axios.post(
				`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/connect/public`,
				{},
				{
					headers: {
						'Content-Type': 'application/json',
						'Authorization': 'Bearer ' + accessToken,
					},
				},
			);
			console.log(response);
			return response.data.token;
		};

		const getSessionId = async (studioId: string): Promise<string> => {
			const response = await axios.get(
				`${OPENVIDU_SERVER_URL}/api/studios/${studioId}`,
				{
					headers: {
						'Content-Type': 'application/json',
						'Authorization': 'Bearer ' + accessToken,
					},
				},
			);
			console.log('getSessionId : ' + response.data.sessionId);
			return response.data.sessionId; // The studioResponse
		};

		const getToken = async (): Promise<string> => {
			try {
				const response = await createSession();
				setStudioId(response.studioId);
				console.log(response);
				console.log(studioId);
				const studioResponse = await getSessionId(response.studioId);
				setSessionId(studioResponse);
				console.log(studioResponse);
				return await response.token;
			} catch (error) {
				throw new Error('Failed to get token.');
			}
		};

		const getSession = async () => {
			try {
				console.log(studioId);
				const searchSessionId = getSessionId(studioId);
				setSessionId(await searchSessionId);
			} catch (error) {
				throw new Error('Failed to get token.');
			}
		};

		// ======

		const createScreenToken = async () => {
			const response = await axios.get(
				`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/connect/share`,
				{
					headers: {
						'Content-Type': 'application/json',
						'Authorization': 'Bearer ' + accessToken,
					},
				},
			);
			console.log(response.data);
			return response.data;
		};

		const getScreenToken = async (): Promise<string> => {
			try {
				const token = await createScreenToken();
				return token;
			} catch (error) {
				console.log(error);
				throw new Error('Failed to get token.');
			}
		};

		if (order === 1) {
			console.log('111111111111');
			getToken()
				.then((token) => {
					session
						.connect(token)
						.then(() => {
							if (OV) {
								const publishers = OV.initPublisher(undefined, {
									audioSource: undefined, // The source of audio. If undefined default microphone
									videoSource: undefined, // The source of video. If undefined default webcam
									publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
									publishVideo: true, // Whether you want to start publishing with your video enabled or not
									resolution: '640x480', // The resolution of your video
									frameRate: 30, // The frame rate of your video
									insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
									mirror: true, // Whether to mirror your local video or not
								});

								setPublisher(publishers);
								session
									.publish(publishers)
									.then(() => {})
									.catch(() => {});
							}
						})
						.catch(() => {});
				})
				.catch(() => {});
		} else if (order === 2) {
			console.log('22222222222');
			getSession().then(() => {
				createToken(studioId)
					.then((token) => {
						session
							.connect(token)
							.then(() => {
								if (OV) {
									const publishers = OV.initPublisher(undefined, {
										audioSource: undefined, // The source of audio. If undefined default microphone
										videoSource: undefined, // The source of video. If undefined default webcam
										publishAudio: true, // Whether you want to start publishing with your audio unmuted or not
										publishVideo: true, // Whether you want to start publishing with your video enabled or not
										resolution: '640x480', // The resolution of your video
										frameRate: 30, // The frame rate of your video
										insertMode: 'APPEND', // How the video is inserted in the target element 'video-container'
										mirror: true, // Whether to mirror your local video or not
									});
									setPublisher(publishers);
									session
										.publish(publishers)
										.then(() => {})
										.catch(() => {});
								}
							})
							.catch(() => {});
					})
					.catch(() => {});
			});
		} else if (order === 3) {
			console.log('333333333333');
			getScreenToken().then((token) => {
				sessionScreen
					.connect(token)
					.then(() => {
						const publisher = screenOV.initPublisher('html-element-id', {
							videoSource: 'screen',
						});

						publisher.once('accessAllowed', (event) => {
							publisher.stream
								.getMediaStream()
								.getVideoTracks()[0]
								.addEventListener('ended', () => {
									console.log('User pressed the "Stop sharing" button');
								});
							sessionScreen.publish(publisher);
						});

						publisher.once('accessDenied', (event) => {
							console.log(error);
							console.warn('ScreenShare: Access Denied');
						});
					})
					.catch((error) => {
						console.warn(
							'There was an error connecting to the session:',
							error.code,
							error.message,
						);
					});
			});
		}
	}, [
		session,
		OV,
		sessionId,
		OPENVIDU_SERVER_URL,
		accessToken,
		studioId,
		order,
		sessionScreen,
		publisher,
		screenOV,
	]);

	return (
		<div>
			<h1>진행화면</h1>
			<>
				{!session && (
					<OpenviduForm
						createSession={CreateSession}
						joinSession={JoinSession}
						studioId={studioId}
						studioIdHandler={studioIdHandler}
					/>
				)}
				{session && (
					<>
						<OpenviduSession
							publisher={publisher as Publisher}
							subscriber={subscriber as Subscriber}
						/>
						<button onClick={() => leaveSession()}>Leave</button>
						<button onClick={() => shareScreen()}>Share</button>
						<button onClick={() => recordingStart()}>RecordingStart</button>
						<button onClick={() => recordingStop()}>RecordingStop</button>
						<button onClick={() => getRecording()}>getRecording</button>
						<Button onClick={handleRecModalOpen}>getRecording</Button>
						<Modal
							open={modalOpen}
							onClose={handleRecModalClose}
							aria-labelledby="modal-modal-title"
							aria-describedby="modal-modal-description"
						>
							<Box sx={modalStyle}>
								<Typography id="modal-modal-title" variant="h6" component="h2">
									녹음파일 다운로드
								</Typography>
								<Typography id="modal-modal-description" sx={{ mt: 2 }}>
									창을 닫으면 녹음파일이 지워집니다.
									{recFiles.map((link, index) => (
										<Button onClick={() => downloadFiles(link)}>
											녹음파일{index + 1}
										</Button>
									))}
								</Typography>
							</Box>
						</Modal>
					</>
				)}
				<div>
					{messages.map((message, index) => (
						<div key={index}>
							<strong>{message.sender}:</strong> {message.message}
						</div>
					))}
					<input
						value={newMessage}
						onChange={(e) => setNewMessage(e.target.value)}
						onKeyPress={(e) => {
							if (e.key === 'Enter') {
								sendMessage();
							}
						}}
						placeholder="메시지를 입력하세요"
						type="text"
					/>
				</div>
			</>
		</div>
	);
}

export default OpenviduMain;
