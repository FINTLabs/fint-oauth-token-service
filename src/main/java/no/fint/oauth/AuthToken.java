package no.fint.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiredUnixTimestampInSeconds,
        @JsonProperty("acr") String acr,
        @JsonProperty("scope") String scope
) {

    public AuthToken(String accessToken, String tokenType, long expiredUnixTimestampInSeconds, String acr, String scope) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiredUnixTimestampInSeconds = System.currentTimeMillis() + (expiredUnixTimestampInSeconds * 1000);
        this.acr = acr;
        this.scope = scope;
    }

}
