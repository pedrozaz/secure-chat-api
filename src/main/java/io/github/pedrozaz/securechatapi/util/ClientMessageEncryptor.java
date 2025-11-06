package io.github.pedrozaz.securechatapi.util;

import io.github.pedrozaz.securechatapi.dto.EncryptedPayload;
import io.github.pedrozaz.securechatapi.service.CryptoService;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

public class ClientMessageEncryptor {
    public static void main(String[] args) throws GeneralSecurityException {

        String sessionId = "4fb504b1-6134-4e89-9044-000301a273ea";
        String sharedSecretBase64 = "tw1XF/efmh+6QDAIWTbHlftCQ/dnZAgWVFMXk7nMJ49qxlXwmmQ48cVkgJ4pXqnfwGG9/XLV+UaE31G7y26KXurSBvnO2U4Mgi9oAJ74YRqDxttHpLEiu9H3Hk5q1NcvYPnQIvRajNly0UPoF0kP4F94q5MeEF3VH5m/iJR2xWwPEj6RGwyVLrZvhDZWzYxgxTUKqiVU+xlM6mcvmY0fqoaZKQxeG91beNjM8Y5hYZGkMzeSJqyoD2kZnxwoBfzEjq5fENvrJyLcMIO2RQB7ygQbG0mOFnb91bH5nfm6QKJ30C3pQNswv5Hau6Ixx3LcppMdJy1gn0N5Bjr42R+D3w==";
        String plaintextMessage = "Hello, secure chat";

        CryptoService cryptoService = new CryptoService();
        byte[] sharedSecret = Base64.getDecoder().decode(sharedSecretBase64);
        byte[] plaintextBytes = plaintextMessage.getBytes(StandardCharsets.UTF_8);

        EncryptedPayload payload = cryptoService.encrypt(sharedSecret, plaintextBytes);

        String ivBase64 = Base64.getEncoder().encodeToString(payload.iv());
        String ciphertextBase64 = Base64.getEncoder().encodeToString(payload.ciphertext());

        System.out.println("----------------------------JSON MODEL-------------------------------------");
        System.out.printf("""
                {
                    "sessionId": "%s",
                    "iv": "%s",
                    "ciphertext": "%s"
                }
                """, sessionId, ivBase64, ciphertextBase64);
    }
}
