package no.fint.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("acr") String acr,
        @JsonProperty("scope") String scope
) {
}
