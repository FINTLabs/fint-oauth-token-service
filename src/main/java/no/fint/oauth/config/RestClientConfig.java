package no.fint.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("oauthRestClient")
    public RestClient oauthRestClient() {
        return RestClient.builder().build();
    }

}
