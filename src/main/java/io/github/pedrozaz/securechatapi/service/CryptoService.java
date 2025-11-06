package io.github.pedrozaz.securechatapi.service;

import io.github.pedrozaz.securechatapi.dto.EncryptedPayload;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

@Service
public class CryptoService {

    public KeyPair generateDHKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public byte[] computeSharedSecret(PrivateKey privateKey, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        return keyAgreement.generateSecret();
    }

    public byte[] decrypt(byte[] sharedSecret, byte[] iv, byte[] ciphertext) throws GeneralSecurityException {
        SecretKeySpec secretKeySpec = createAesKeyFromSharedSecret(sharedSecret);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

        return cipher.doFinal(ciphertext);
    }

    public EncryptedPayload encrypt(byte[] sharedSecret, byte[] plaintext) throws GeneralSecurityException {
        SecretKeySpec secretKeySpec = createAesKeyFromSharedSecret(sharedSecret);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
        byte[] ciphertext = cipher.doFinal(plaintext);

        return new EncryptedPayload(iv, ciphertext);
    }

    private SecretKeySpec createAesKeyFromSharedSecret(byte[] sharedSecret) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha256.digest(sharedSecret);
        return new SecretKeySpec(keyBytes, "AES");
    }
}
