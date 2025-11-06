package io.github.pedrozaz.securechatapi.service;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

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
        byte[] keyBytes = new byte[32];
        System.arraycopy(sharedSecret, 0, keyBytes, 0, keyBytes.length);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);

        return cipher.doFinal(ciphertext);
    }
}
