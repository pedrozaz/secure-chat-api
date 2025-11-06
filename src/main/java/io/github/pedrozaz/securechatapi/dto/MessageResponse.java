package io.github.pedrozaz.securechatapi.dto;

public record MessageResponse(
        String status,
        String decryptedData
) {
}
