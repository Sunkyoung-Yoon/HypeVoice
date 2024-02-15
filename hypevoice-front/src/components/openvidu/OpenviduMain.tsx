import React from "react";
import { useState, useEffect, useCallback } from "react";
import {
  OpenVidu,
  Session as OVSession,
  Publisher,
  Subscriber,
} from "openvidu-browser";
import axios from "axios";
import OpenviduForm from "./OpenviduForm";
import OpenviduSession from "./OpenviduSession";
import { getCookie } from "../../api/cookie";
import { RtcInfo } from "@/recoil/RealTimeComm";
import { useRecoilState, useRecoilValue } from "recoil";
import ExitToAppIcon from "@mui/icons-material/ExitToApp";
import ScreenShareIcon from "@mui/icons-material/ScreenShare";
import MicIcon from "@mui/icons-material/Mic";
import StopCircleIcon from "@mui/icons-material/StopCircle";
import { Box, Button, Modal, Typography } from "@mui/material";
import { CurrentMemberAtom } from "@/recoil/Auth";
import { useNavigate } from "react-router-dom";
import { title } from "process";

const modalStyle = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  height: 400,
  bgcolor: "background.paper",
  border: "2px solid #000",
  boxShadow: 24,
  p: 4,
};

// 채팅 타입
type ChatType = {
  sender: string;
  message: string;
};

function OpenviduMain() {
  const OPENVIDU_SERVER_URL = `http://localhost:8081`;
  const [session, setSession] = useState<OVSession | "">("");
  const [sessionScreen, setSessionScreen] = useState<OVSession | "">("");
  const [studioId, setStudioId] = useState<string>("");
  const [sessionId, setSessionId] = useState<string>("");
  const [subscriber, setSubscriber] = useState<Subscriber | null>(null);
  const [publisher, setPublisher] = useState<Publisher | null>(null);
  const [OV, setOV] = useState<OpenVidu | null>(null);
  const [screenOV, setScreenOV] = useState<OpenVidu | null>(null);
  const [order, setOrder] = useState<number>(0);
  const [newMessage, setNewMessage] = useState<string>("");
  const [recordingId, setRecordingId] = useState<string>("");
  const [messages, setMessages] = useState<ChatType[]>([]);
  const [isHost, setIsHost] = useState<boolean>(true);
  const [isRecording, setIsRecording] = useState<boolean>(false);
  const [modalOpen, setModalOpen] = React.useState(false);
  const [recFiles, setRecFiles] = useState<string[]>([]);
  const [rtcInfo, setRtcInfo] = useRecoilState(RtcInfo);
  const currentMember = useRecoilValue(CurrentMemberAtom);
  const navigate = useNavigate();

  // 녹음 모달 열기
  const handleRecModalOpen = async () => {
    setRecFiles(await getRecording());
    setModalOpen(true);
  };

  // 녹음 모달 닫기(화면 밖 클릭 시에도!)
  const handleRecModalClose = async () => {
    console.log(recFiles);
    await axios
      .delete(
        `${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/${recordingId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          data: { urls: recFiles },
        }
      )
      .catch((e) => console.log(e))
      .then(() => {
        setRecFiles([]);
      });
    setModalOpen(false);
  };

  // const OPENVIDU_SERVER_SECRET = 'MY_SECRET';
  // 방 나가면 DB에서 자동으로 삭제되도록 하는 함수
  const handleDelete = async (): Promise<void> => {
    try {
      const accessToken = getCookie("access_token");
      await axios.delete(`${OPENVIDU_SERVER_URL}/api/studios/${studioId}`, {
        headers: { Authorization: `Bearer ${accessToken}` },
      });
    } catch (error) {
      console.log(error);
    }
  };

  // 엑세스 토큰
  const accessToken = getCookie("access_token");

  // 방 나가기
  const leaveSession = useCallback(() => {
    setOrder(0);
    if (session) session.disconnect();
    setOV(null);
    setScreenOV(null);
    setSession("");
    setSessionScreen("");
    setSessionId("");
    setSubscriber(null);
    setPublisher(null);
    setMessages([]);
    if (isHost) handleDelete();
  }, [session]);

  // 방 만들기
  const CreateSession = () => {
    setOrder(1);
    const OVs = new OpenVidu();
    setOV(OVs);
    setSession(OVs.initSession());
  };

  // 방 참가하기
  const JoinSession = () => {
    setOrder(2);
    const OVs = new OpenVidu();
    setOV(OVs);
    setSession(OVs.initSession());
  };

  // 화면 공유
  const shareScreen = () => {
    setOrder(3);
    const OVs = new OpenVidu();
    setScreenOV(OVs);
    setSessionScreen(OVs.initSession());
    console.log("share");
  };

  // 녹음 시작
  const recordingStart = async () => {
    if (session === "") return;
    const response = await axios
      .post(
        `${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/start`,
        {},
        {
          params: { isIndividual: false },
          headers: {
            Authorization: "Bearer " + accessToken,
          },
        }
      )
      .catch((e) => console.log(e));

    console.log("녹음시작");
    session.signal({
      to: [],
      type: "recordstart",
    });

    setRecordingId(response.data);
    return response.data;
  };

  // 녹음 중지 => 자동 파일화
  const recordingStop = async () => {
    if (session === "") return;
    const response = await axios.post(
      `${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/stop/${recordingId}`,
      {},
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + accessToken,
        },
      }
    );
    session.signal({
      to: [],
      type: "recordstart",
    });
    console.log("녹음완료 => 녹음결과 : ", response);
    return response;
  };

  // 녹음 링크 가져오기
  const getRecording = async (): Promise<string[]> => {
    const response = await axios.get(
      `${OPENVIDU_SERVER_URL}/api/studios/${studioId}/recording/${recordingId}`,
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + accessToken,
        },
      }
    );
    console.log("받아온 결과 : ", response);
    return response.data;
  };

  // 파일 다운로드
  const downloadFiles = async (fileUrl: string) => {
    try {
      // 각 파일 URL에 대해 Promise를 생성하여 배열에 저장합니다.
      const response = await axios({
        url: fileUrl,
        method: "GET",
        responseType: "blob", // Blob 형태로 응답 받습니다.
      });
      const blob = new Blob([response.data]);
      const url = window.URL.createObjectURL(blob);
      const filename = getFileNameFromUrl(fileUrl);

      // 다운로드 링크를 생성하고 클릭합니다.
      const a = document.createElement("a");
      a.href = url;
      a.download = filename; // 다운로드될 파일명 설정
      document.body.appendChild(a);
      a.click();

      // 사용이 끝난 URL 객체를 해제합니다.
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading files:", error);
    }
  };

  // 파일 URL에서 파일명을 추출합니다.
  const getFileNameFromUrl = (fileUrl: string) => {
    const startIndex = fileUrl.lastIndexOf("/") + 1;
    const filename = fileUrl.substring(startIndex);
    return filename;
  };

  // 제일 첫 Hook
  useEffect(() => {
    console.log("rtcInfo in OV");
    console.log(rtcInfo.roomRole);
    if (rtcInfo.roomRole) CreateSession();
    else JoinSession();
  }, []);

  // 윈도우 꺼지기 전에 세션부터 나감
  useEffect(() => {
    window.addEventListener("beforeunload", leaveSession);

    return () => {
      window.removeEventListener("beforeunload", leaveSession);
    };
  }, [leaveSession]);

  // 스튜디오 자체에 관한 핸들러
  const studioIdHandler = (event: React.ChangeEvent<HTMLInputElement>) => {
    setStudioId(event.target.value);
  };

  // 메시지 전송
  const sendMessage = () => {
    if (session === "") return; // 세션이 없거나
    if (newMessage.trim() === "") return; // 빈 문자열인 경우 전송하지 않음

    session.signal({
      data: newMessage,
      to: [],
      type: "chat",
    });

    setNewMessage("");
  };

  // 채팅 관련
  useEffect(() => {
    if (session === "") return;

    const handleChatSignal = (event) => {
      setMessages((messages) => [
        ...messages,
        {
          sender: event.from.connectionId,
          message: event.data,
        },
      ]);
    };
    // 내가 적은 메시지를 내가 보냈다 + 해당 내용으로 설정

    session.on("signal:chat", handleChatSignal);

    return () => {
      session.off("signal:chat", handleChatSignal);
    };
  }, [session]);

  useEffect(() => {
    if (session === "") return;

    // 내가 녹화중이니깐 다른 사람들은 못 누른다는 걸
    // 알려주는 내용을 담아서 시그널로 보냄
    const handleRecordSignal = (event) => {
      setIsRecording((isRecording) => {
        return !isRecording;
      });
    };
    session.on("signal:recordstart", handleRecordSignal);

    return () => {
      session.off("signal:recordstart", handleRecordSignal);
    };
  }, [session]);

  // 인원 관리 // 구독자 나가면 나간 거 처리
  useEffect(() => {
    if (session === "") return;

    session.on("streamDestroyed", (event) => {
      if (subscriber && event.stream.streamId === subscriber.stream.streamId) {
        setSubscriber(null);
      }
    });
  }, [subscriber, session]);

  // 전체 관리
  useEffect(
    () => {
      // 세션이 있으면 세션 관련 설정
      if (session != "") {
        session.on("streamCreated", (event) => {
          if (event.stream.typeOfVideo == "CAMERA") {
            const subscribers = session.subscribe(
              event.stream,
              "container-cameras"
            );
            setSubscriber(subscribers);
          }
        });
      }

      // 화면 공유 세션이 있으면 관련 설정
      if (sessionScreen != "") {
        sessionScreen.on("streamCreated", (event) => {
          if (event.stream.typeOfVideo == "SCREEN") {
            const subscribers = sessionScreen.subscribe(
              event.stream,
              "container-screens"
            );
            setSubscriber(subscribers);
          }
        });
      }

      // 방 만들고 토큰 발급받는 부분
      const createSession = async () => {
        if (rtcInfo.isPublic === 1) {
          console.log(`rtcInfo.title = ${rtcInfo.title}`);
          console.log(`rtcInfo.intro = ${rtcInfo.intro}`);
          console.log(`rtcInfo.limitNumber = ${rtcInfo.limitNumber}`);
          console.log(`rtcInfo.isPublic = ${rtcInfo.isPublic}`);
        } else {
          alert("비밀방은 유료 서비스입니다!");
          navigate("/studioList");
          return;
        }

        const response = await axios.post(
          `${OPENVIDU_SERVER_URL}/api/studios`,
          {
            title: rtcInfo.title,
            intro: rtcInfo.intro,
            limitNumber: rtcInfo.limitNumber,
            isPublic: rtcInfo.isPublic,
            passwrod: null, // 공개방이면 null인데 오류 발생 가능
          },
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + accessToken,
            },
          }
        );
        return response.data;
      };

      // 방 생성 시 정보를 가지고 토큰 발급 후
      // 구독자들 참가 시 이용
      const createToken = async (studioId: string) => {
        console.log(`studioId = ${studioId}`);
        const response = await axios.post(
          `${OPENVIDU_SERVER_URL}/api/studios/${studioId}/connect/public`,
          {},
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + accessToken,
            },
          }
        );
        console.log(response);
        return response.data.token;
      };

      // 세션 아이디 생성
      // 방 자체의 번호 (토큰과는 다름!)
      const getSessionId = async (studioId: number): Promise<string> => {
        const response = await axios.get(
          `${OPENVIDU_SERVER_URL}/api/studios/${studioId}`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + accessToken,
            },
          }
        );
        console.log("getSessionId : " + response.data.sessionId);
        return response.data.sessionId; // The studioResponse
      };

      // 토큰 받는 함수
      const getToken = async (): Promise<string> => {
        try {
          // 세션을 만들고 그걸로 studioId를 설정
          const response = await createSession();
          console.log(typeof response.studioId);
          console.log(response.studioId);
          setRtcInfo({
            roomRole: rtcInfo.roomRole,
            studioId: response.studioId,
            title: rtcInfo.title,
            intro: rtcInfo.intro,
            limitNumber: rtcInfo.limitNumber,
            isPublic: rtcInfo.isPublic,
            password: null,
          });
          setStudioId(response.studioId);
          // 그 studioId를 가지고 sessionId를 받아서 설정 << 얘가 진짜 방 번호
          const studioResponse = await getSessionId(response.studioId);
          setSessionId(studioResponse);
          console.log("studioResponse in OV");
          console.log(typeof studioResponse);
          console.log(studioResponse);

          // 그 안에 담긴 토큰을 반환
          return await response.token;
        } catch (error) {
          console.log("error!");
          throw new Error("Failed to get token.");
        }
      };

      // 방 번호로 해당 진짜 방 번호 받기
      const getSession = async () => {
        try {
          const searchSessionId = getSessionId(studioId);
          setSessionId(await searchSessionId);
        } catch (error) {
          throw new Error("Failed to get token.");
        }
      };

      // ======[화면 공유 관련]=======

      // 화면 공유용 토큰 생성
      const createScreenToken = async () => {
        const response = await axios.get(
          `${OPENVIDU_SERVER_URL}/api/studios/${studioId}/connect/share`,
          {
            headers: {
              "Content-Type": "application/json",
              Authorization: "Bearer " + accessToken,
            },
          }
        );
        return response.data;
      };

      // 화면 공유용 토큰 받기
      const getScreenToken = async (): Promise<string> => {
        try {
          const token = await createScreenToken();
          return token;
        } catch (error) {
          console.log(error);
          throw new Error("Failed to get token.");
        }
      };

      //////////////////////////
      //////////////////////////
      ///여기서 부터 메인 로직///
      //////////////////////////
      //////////////////////////

      // 방 생성
      if (order === 1) {
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
                    resolution: "640x480", // The resolution of your video
                    frameRate: 30, // The frame rate of your video
                    insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
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
      // 방 참여
      else if (order === 2) {
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
                      resolution: "640x480", // The resolution of your video
                      frameRate: 30, // The frame rate of your video
                      insertMode: "APPEND", // How the video is inserted in the target element 'video-container'
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
      }
      // 화면 공유
      else if (order === 3) {
        getScreenToken().then((token) => {
          sessionScreen
            .connect(token)
            .then(() => {
              const publisher = screenOV.initPublisher("html-element-id", {
                videoSource: "screen",
              });

              publisher.once("accessAllowed", (event) => {
                publisher.stream
                  .getMediaStream()
                  .getVideoTracks()[0]
                  .addEventListener("ended", () => {
                    console.log('User pressed the "Stop sharing" button');
                  });
                sessionScreen.publish(publisher);
              });

              publisher.once("accessDenied", (event) => {
                console.log(error);
                console.warn("ScreenShare: Access Denied");
              });
            })
            .catch((error) => {
              console.warn(
                "There was an error connecting to the session:",
                error.code,
                error.message
              );
            });
        });
      }
    },
    // 관리 되는 상태들
    [
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
    ]
  );

  return (
    <div style={{ backgroundColor: "#202124" }}>
      <h1>{rtcInfo.title}</h1>
      <>
        {!session && (
          <OpenviduForm
            createSession={CreateSession}
            joinSession={JoinSession}
            studioId={studioId}
            studioIdHandler={studioIdHandler}
          />
        )}
        {/* 세션이 있으면 화면 표시 */}
        {session && (
          <>
            <OpenviduSession
              publisher={publisher as Publisher}
              subscriber={subscriber as Subscriber}
            />
            <button
              onClick={() => leaveSession()}
              style={{ borderRadius: "50px", backgroundColor: "#333333" }}
            >
              <ExitToAppIcon fontSize="large" sx={{ color: "red" }} />
            </button>
            <button
              onClick={() => shareScreen()}
              style={{ borderRadius: "50px", backgroundColor: "#333333" }}
            >
              <ScreenShareIcon fontSize="large" sx={{ color: "#5b5ff4" }} />
            </button>
            {!isRecording && (
              <button
                onClick={() => recordingStart()}
                style={{ borderRadius: "50px", backgroundColor: "red" }}
              >
                <MicIcon fontSize="large" sx={{ color: "white" }} />
              </button>
            )}
            {isRecording && (
              <button
                onClick={() => recordingStop()}
                style={{ borderRadius: "50px", backgroundColor: "red" }}
              >
                <StopCircleIcon fontSize="large" sx={{ color: "white" }} />
              </button>
            )}
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
              if (e.key === "Enter") {
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
