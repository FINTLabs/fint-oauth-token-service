package no.fint.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expirationTimestampMillis,
        @JsonProperty("acr") String acr,
        @JsonProperty("scope") String scope
) {

    public AuthToken(String accessToken, String tokenType, long expirationTimestampMillis, String acr, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expirationTimestampMillis = System.currentTimeMillis() + (expirationTimestampMillis * 1000);
        this.acr = acr;
        this.scope = scope;
    }

}
