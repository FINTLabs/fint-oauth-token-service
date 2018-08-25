package no.fint.oauth

import spock.lang.Specification

class OAuthRestTemplateFactorySpec extends Specification {

    def "Create new OAuth2 rest template"() {
        given:
        def factory = new OAuthRestTemplateFactory(props: new OAuthTokenProps(scope: 'scope'))

        when:
        def restTemplate = factory.create('username', 'password', 'clientId', 'clientSecret')

        then:
        restTemplate.resource.scope[0] == 'scope'
        restTemplate.resource.clientId == 'clientId'
    }
}
