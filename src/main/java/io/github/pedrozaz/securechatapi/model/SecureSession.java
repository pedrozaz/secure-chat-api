package io.github.pedrozaz.securechatapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "session")
@Getter @Setter
@NoArgsConstructor
public class SecureSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 1024)
    private byte[] sharedSecret;

    public SecureSession(byte[] sharedSecret) {
        this.sharedSecret = sharedSecret;
    }
}
