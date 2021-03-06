# FINT OAuth Token Service 

[![Build Status](https://travis-ci.org/FINTLabs/fint-oauth-token-service.svg?branch=master)](https://travis-ci.org/FINTLabs/fint-oauth-token-service)
[![Coverage Status](https://coveralls.io/repos/github/FINTLabs/fint-oauth-token-service/badge.svg?branch=master)](https://coveralls.io/github/FINTLabs/fint-oauth-token-service?branch=master)
[ ![Download](https://api.bintray.com/packages/fint/maven/fint-oauth-token-service/images/download.svg) ](https://bintray.com/fint/maven/fint-oauth-token-service/_latestVersion)

Based on the [Spring Security OAuth](http://projects.spring.io/spring-security-oauth/) project.  
Handles the access and refresh token.


## Installation

```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/fint/maven" 
    }
}

compile('no.fint:fint-oauth-token-service:+')
```

## Usage

### TokenService

`TokenService` is usually used with SSE.  
Import the `OAuthConfig` class fro the `@Configuration`.

```java
@Import(OAuthConfig.class)
@Configuration
public class Config {
    ...
}
```

Autoimport the `TokenService` and call `getAccessToken()`.  
If the property `fint.oauth.enabled` is set to `false` the `TokenService` will be null.

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

#### TokenService Configuration

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
| fint.oauth.grant-type | Grant type, optional (default value: password) |

---

### RestTemplate Factory

`OAuthRestTemplateFactory` is used to create new instances of `OAuth2RestTemplate`.

Import the `OAuthConfig` class fro the `@Configuration`.

```java
@Import(OAuthConfig.class)
@Configuration
public class Config {
    ...
}
```

Autoimport the `OAuthRestTemplateFactory` and call `create(username, password, clientId, clientSecret)`.  
If the property `fint.oauth.enabled` is set to `false` the `OAuthRestTemplateFactory` will be null.

```java
@Autowired(required = false)
private OAuthRestTemplateFactory factory;

public void myMethod() {
    if(factory != null) {
        OAuth2RestTemplate restTemplate = factory.create(username, password, clientId, clientSecret);
        ...
    }
}
```

#### RestTemplate Factory Configuration

| Key | Description |
|-----|-------------|
| fint.oauth.enabled | true / false. Enables / disables the OAuthRestTemplateFactory. Disabled by default. |
| fint.oauth.access-token-uri | Access token URI |
| fint.oauth.scope | Scope |
| fint.oauth.grant-type | Grant type (default value: password) |
