package hypevoice.hypevoiceback.studio.dto;

public record StudioMessageRequest(String sessionId, String sender, String message, String timestamp) {
}
