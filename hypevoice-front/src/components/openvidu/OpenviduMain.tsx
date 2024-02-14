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

function OpenviduMain() {
	const [session, setSession] = useState<OVSession | ''>('');
	const [studioId, setStudioId] = useState<string>('');
	const [sessionId, setSessionId] = useState<string>('');
	const [subscriber, setSubscriber] = useState<Subscriber | null>(null);
	const [publisher, setPublisher] = useState<Publisher | null>(null);
	const [OV, setOV] = useState<OpenVidu | null>(null);
	const [isCreate, setIsCreate] = useState<boolean>(true);
	const OPENVIDU_SERVER_URL = `http://localhost:8081`;

	// const OPENVIDU_SERVER_SECRET = 'MY_SECRET';

	const accessToken = getCookie('access_token');

	const leaveSession = useCallback(() => {
		if (session) session.disconnect();
		setOV(null);
		setSession('');
		setSessionId('');
		setSubscriber(null);
		setPublisher(null);
	}, [session]);

	const CreateSession = () => {
		setIsCreate(true);
		const OVs = new OpenVidu();
		setOV(OVs);
		setSession(OVs.initSession());
	};

	const JoinSession = () => {
		setIsCreate(false);
		const OVs = new OpenVidu();
		setOV(OVs);
		setSession(OVs.initSession());
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

	useEffect(() => {
		if (session === '') return;

		session.on('streamDestroyed', (event) => {
			if (subscriber && event.stream.streamId === subscriber.stream.streamId) {
				setSubscriber(null);
			}
		});
	}, [subscriber, session]);

	useEffect(() => {
		if (session === '') return;

		session.on('streamCreated', (event) => {
			const subscribers = session.subscribe(event.stream, '');
			setSubscriber(subscribers);
		});

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
			const response = await axios
				.post(
					`${OPENVIDU_SERVER_URL}/api/studios/${studioId}/connect/public`,
					{},
					{
						headers: {
							'Content-Type': 'application/json',
							'Authorization': 'Bearer ' + accessToken,
						},
					},
				)
				.catch((e) => console.log(e));
			console.log(response);
			return response.data.token; // The token
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
				console.log(sessionId);
				return await response.token;
			} catch (error) {
				throw new Error('Failed to get token.');
			}
		};

		const getSession = async () => {
			try {
				const searchSessionId = getSessionId(studioId);
				setSessionId(await searchSessionId);
			} catch (error) {
				throw new Error('Failed to get token.');
			}
		};

		if (!isCreate) {
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
		} else {
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
		}
	}, [session, OV, sessionId, OPENVIDU_SERVER_URL, accessToken, studioId]);

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
					</>
				)}
			</>
		</div>
	);
}

export default OpenviduMain;
