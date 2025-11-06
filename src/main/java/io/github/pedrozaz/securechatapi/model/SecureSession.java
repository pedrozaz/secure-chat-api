package io.github.pedrozaz.securechatapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class SecureSession {
    @Id
    private String id;

    @Column(nullable = false, length = 1024)
    private byte[] sharedSecret;

    public SecureSession(String publicKey, byte[] sharedSecret) {
        this.id = id;
        this.sharedSecret = sharedSecret;
    }
}
