package io.github.pedrozaz.securechatapi.controller;

import io.github.pedrozaz.securechatapi.dto.HandshakeRequest;
import io.github.pedrozaz.securechatapi.dto.HandshakeResponse;
import io.github.pedrozaz.securechatapi.model.SecureSession;
import io.github.pedrozaz.securechatapi.repository.SecureSessionRepository;
import io.github.pedrozaz.securechatapi.service.CryptoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/handshake")
public class HandshakeController {

    private final CryptoService cryptoService;
    private final SecureSessionRepository sessionRepository;

    public HandshakeController(CryptoService cryptoService, SecureSessionRepository sessionRepository) {
        this.cryptoService = cryptoService;
        this.sessionRepository = sessionRepository;
    }

    @PostMapping("/start")
    public HandshakeResponse startHandshake(@RequestBody HandshakeRequest request)
        throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {

        byte[] clientPublicKeyBytes = Base64.getDecoder().decode(request.publicKey());
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        PublicKey clientPublicKey = keyFactory.generatePublic(new X509EncodedKeySpec(clientPublicKeyBytes));

        KeyPair serverKeyPair = cryptoService.generateDHKeyPair();

        byte[] sharedSecret = cryptoService.computeSharedSecret(serverKeyPair.getPrivate(),
                serverKeyPair.getPublic());

        String sessionId = UUID.randomUUID().toString();
        SecureSession session = new SecureSession(sessionId, sharedSecret);
        sessionRepository.save(session);

        String serverPublicKeyBase64 = Base64.getEncoder().encodeToString(serverKeyPair.getPublic().getEncoded());

        return new HandshakeResponse(sessionId, serverPublicKeyBase64);
    }
}
