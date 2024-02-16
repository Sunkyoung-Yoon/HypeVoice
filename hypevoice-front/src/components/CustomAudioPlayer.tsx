export default function CustomAudioPlayer({
  src,
  width,
}: {
  src: string;
  width: number;
}) {
  return (
    <audio
      controls
      style={{
        width: `${width}%`,
        border: "3px solid black",
        borderRadius: "25px",
      }}
    >
      <source src={src} type="audio/mpeg" />
      Your browser does not support the audio element.
    </audio>
  );
}