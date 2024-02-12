export default function CustomAudioPlayer({ src }: { src: string }) {
  return (
    <audio
      controls
      style={{
        width: "100%",
        border: "3px solid black",
        borderRadius: "25px",
      }}
    >
      <source src={src} type="audio/mpeg" />
      Your browser does not support the audio element.
    </audio>
  );
}
