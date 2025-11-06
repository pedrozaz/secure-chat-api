package io.github.pedrozaz.securechatapi.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ClientKeyGenerator {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DH");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String clientPublicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());

        System.out.println("-------------------------------------------");
        System.out.println("Client Public Key: " + clientPublicKeyBase64);
        System.out.println("-------------------------------------------");

    }
}
