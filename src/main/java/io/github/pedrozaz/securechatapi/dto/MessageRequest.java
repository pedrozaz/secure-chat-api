package io.github.pedrozaz.securechatapi.dto;

public record MessageRequest(
        String sessionId,
        String iv,
        String ciphertext
) {
}
