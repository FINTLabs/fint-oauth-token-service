package no.fint.oauth

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.common.OAuth2AccessToken
import spock.lang.Specification

class TokenServiceSpec extends Specification {
    private TokenService tokenService
    private OAuthTokenProps props
    private OAuth2RestTemplate restTemplate

    void setup() {
        restTemplate = Mock(OAuth2RestTemplate)
        props = Mock(OAuthTokenProps)
        tokenService = new TokenService(props: props, restTemplate: restTemplate)
    }

    def "Throw IllegalStateException if request url does not return OK status"() {
        when:
        tokenService.init()

        then:
        2 * props.getRequestUrl() >> 'invalid-url'
        1 * restTemplate.getForEntity(_ as String, _ as Class) >> ResponseEntity.notFound().build()
        thrown(IllegalStateException)
    }

    def "Get AccessToken value if expiration is more than 5 seconds"() {
        when:
        def accessToken = tokenService.getAccessToken()

        then:
        1 * restTemplate.getAccessToken() >> Mock(OAuth2AccessToken) {
            getExpiresIn() >> 10
            getValue() >> 'test'
        }
        accessToken == 'test'
    }

    def "Refresh AccessToken if expiration is less than 5 seconds"() {
        when:
        def accessToken = tokenService.getAccessToken()

        then:
        2 * restTemplate.getAccessToken() >> Mock(OAuth2AccessToken) {
            getExpiresIn() >> 4
            getValue() >> 'test'
        }
        1 * props.getRequestUrl() >> 'http://localhost'
        1 * restTemplate.getForEntity(_ as String, _ as Class) >> ResponseEntity.ok().build()
        accessToken == 'test'
    }
}
