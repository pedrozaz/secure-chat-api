package io.github.pedrozaz.securechatapi.repository;

import io.github.pedrozaz.securechatapi.model.SecureSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecureSessionRepository extends JpaRepository<SecureSession, String> {

}
