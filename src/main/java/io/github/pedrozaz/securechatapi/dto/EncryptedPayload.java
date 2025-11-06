package io.github.pedrozaz.securechatapi.dto;

public record EncryptedPayload(
        byte[] iv, byte[] ciphertext
) {
}
