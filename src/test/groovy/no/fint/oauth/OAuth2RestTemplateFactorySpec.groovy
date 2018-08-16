package no.fint.oauth


import spock.lang.Specification

class OAuth2RestTemplateFactorySpec extends Specification {

    def "Create new rest template"() {
        when:
        def restTemplate = OAuth2RestTemplateFactory.create(new OAuthTokenProps())

        then:
        restTemplate
    }
}
