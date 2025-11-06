package io.github.pedrozaz.securechatapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public record HandshakeRequest(String publicKey) {
}
