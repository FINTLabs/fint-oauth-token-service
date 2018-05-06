# FINT OAuth Token Service

[![Build Status](https://travis-ci.org/FINTlibs/fint-oauth-token-service.svg?branch=master)](https://travis-ci.org/FINTlibs/fint-oauth-token-service)
[![Coverage Status](https://coveralls.io/repos/github/FINTlibs/fint-oauth-token-service/badge.svg?branch=master)](https://coveralls.io/github/FINTlibs/fint-oauth-token-service?branch=master)

Based on the [Spring Security OAuth](http://projects.spring.io/spring-security-oauth/) project.  
Handles the access and refresh token.


## Installation

```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven" 
    }
}

compile('no.fint:fint-oauth-token-service:1.1.0')
```

## Usage

Import the `OAuthConfig` class fro the `@Configuration`.

```java
@Import(OAuthConfig.class)
@Configuration
public class Config {
    ...
}
```

Autoimport the `TokenService` and call `getAccessToken()`.  
If the property `fint.oauth.enabled` is set to `false` the `TokenService` can be null.

```java
@Autowired(required = false)
private TokenService tokenService;

public void myMethod() {
    if(tokenService != null) {
        String accessToken = tokenService.getAccessToken();
        ...
    }
}
```

## Configuration

| Key | Description |
|-----|-------------|
| fint.oauth.enabled | true / false. Enables / disables the TokenService. Disabled by default. |
| fint.oauth.username | Username |
| fint.oauth.password | Password |
| fint.oauth.access-token-uri | Access token URI |
| fint.oauth.client-id | Client id |
| fint.oauth.client-secret | Client secret |
| fint.oauth.request-url | Request url |
| fint.oauth.scope | Scope |
