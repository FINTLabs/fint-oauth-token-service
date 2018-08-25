package no.fint.oauth

import spock.lang.Specification

class OAuthConfigSpec extends Specification {
    private OAuthConfig config

    void setup() {
        config = new OAuthConfig()
    }

    def "Create OAuth props"() {
        when:
        def props = config.props()

        then:
        props != null
    }

    def "Create OAuth RestTemplate"() {
        when:
        def restTemplate = config.oauth2RestTemplate()

        then:
        restTemplate != null
    }

    def "Create TokenService"() {
        when:
        def tokenService = config.tokenService()

        then:
        tokenService != null
    }
}
