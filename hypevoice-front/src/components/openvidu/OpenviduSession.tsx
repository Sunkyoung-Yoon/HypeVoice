import React from "react";
import { useState, useEffect } from "react";
import { Publisher, Subscriber } from "openvidu-browser";
import OpenviduVideo from "./OpenviduVideo";

interface SessionProps {
  subscriber: Subscriber;
  publisher: Publisher;
}

function OpenviduSession({ subscriber, publisher }: SessionProps) {
  const [subscribers, setSubscribers] = useState<Subscriber[]>([]);
  const [audioEnabled, setAudioEnabled] = useState(true);
  const [videoEnabled, setVideoEnabled] = useState(true);

  useEffect(() => {
    // 구독자가 입장 또는 퇴장시마다 구독자 재설정
    if (subscriber) {
      setSubscribers((prevSubscribers) => [...prevSubscribers, subscriber]);
    }
  }, [subscriber]);

  // 구독자 수가 1명 즉 나 밖에 없으면 그냥 화면을 센터에
  // 아니면 normal 하게! (화면을 2개의 컬럼으로 나눠서 하나씩 배치)
  const adjustGridPlacement = (subscriberCount: number) => {
    if (subscriberCount <= 1) {
      return "center";
    }
    return "normal";
  };

  // 음성 켜기/꺼기 상태 관리 핸들러
  const audioMuteHandler = () => {
    setAudioEnabled(!audioEnabled);
    if (publisher) publisher.publishAudio(audioEnabled);
  };

  // 영상 켜기/꺼기 상태 관리 핸들러
  const videoMuteHandler = () => {
    setVideoEnabled(!videoEnabled);
    if (publisher) publisher.publishVideo(videoEnabled);
  };

  // 구독자 수에 따라 화면의 배치를 조정
  const renderSubscribers = () => {
    const gridPlacement = adjustGridPlacement(subscribers.length);

    return (
      <div
        style={{
          display: "grid",
          //  1명일때만 센터
          // 나머지는 2열 횡대
          gridTemplateColumns: gridPlacement === "center" ? "1fr" : "1fr 1fr",
          gap: "20px",
          padding: "15px",
          border: "5px solid white",
        }}
      >
        <div style={{ border: "10px solid #5b5ff4" }}>
          <OpenviduVideo
            streamManager={publisher}
            audioMuteHandler={audioMuteHandler}
            videoMuteHandler={videoMuteHandler}
          />
        </div>
        {subscribers.map((subscriberItem) => (
          <div key={subscriberItem.id}>
            <OpenviduVideo
              streamManager={subscriberItem}
              audioMuteHandler={audioMuteHandler}
              videoMuteHandler={videoMuteHandler}
            />
          </div>
        ))}
      </div>
    );
  };

  return <>{renderSubscribers()}</>;
}

export default OpenviduSession;
