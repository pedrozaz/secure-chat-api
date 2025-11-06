package io.github.pedrozaz.securechatapi.controller;

import io.github.pedrozaz.securechatapi.dto.MessageRequest;
import io.github.pedrozaz.securechatapi.dto.MessageResponse;
import io.github.pedrozaz.securechatapi.model.SecureSession;
import io.github.pedrozaz.securechatapi.repository.SecureSessionRepository;
import io.github.pedrozaz.securechatapi.service.CryptoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final CryptoService cryptoService;
    private final SecureSessionRepository repository;

    public ChatController(CryptoService cryptoService, SecureSessionRepository repository) {
        this.cryptoService = cryptoService;
        this.repository = repository;
    }

    @PostMapping("/message")
    public MessageResponse handleMessage(@RequestBody MessageRequest messageRequest) {
        SecureSession session = repository.findById(messageRequest.sessionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found"));
        byte[] sharedSecret = session.getSharedSecret();
        byte[] iv = Base64.getDecoder().decode(messageRequest.iv());
        byte[] ciphertext = Base64.getDecoder().decode(messageRequest.ciphertext());

        try {
            byte[] decryptedBytes = cryptoService.decrypt(sharedSecret, iv, ciphertext);
            String decryptedMessage = new String(decryptedBytes, StandardCharsets.UTF_8);

            System.out.printf("[Session %s] Received message: %s\n", session.getId(), decryptedMessage);

            return new MessageResponse("Message received and decrypted successfully", decryptedMessage);
        } catch (GeneralSecurityException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Decryption failed. Invalid data or key.", e);
        }
    }
}
