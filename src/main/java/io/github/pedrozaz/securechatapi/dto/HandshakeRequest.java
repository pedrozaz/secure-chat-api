package io.github.pedrozaz.securechatapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record HandshakeRequest(
        @JsonProperty String publicKey) {
}
