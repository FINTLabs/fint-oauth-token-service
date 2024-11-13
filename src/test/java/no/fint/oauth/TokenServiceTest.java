package no.fint.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private OAuthTokenProps props;

    @Mock
    private RestClient restClient;

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.RequestBodySpec requestBodySpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenService(props, restClient);
    }

    private OngoingStubbing<ResponseEntity<AuthToken>> mockRestClient(String url) {
        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(eq(url))).thenReturn(requestBodySpec);
        when(requestBodySpec.body(any(MultiValueMap.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        return when(responseSpec.toEntity(AuthToken.class));
    }

    @Test
    void dontInitializeAccessTokenWithoutUrl() {
        // Given
        when(props.getRequestUrl()).thenReturn("");

        // When
        tokenService.init();

        // Then
        verify(restClient, never()).post();
    }

    @Test
    void throwIllegalStateExceptionIfResponseIsNotOk() {
        // Given
        String invalidUrl = "http://invalid-url";
        when(props.getRequestUrl()).thenReturn(invalidUrl);
        mockRestClient(invalidUrl).thenReturn(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, tokenService::init);
        assertThat(exception.getMessage(), containsString("Unable to get access token"));
    }

    @Test
    void getAccessTokenValueIfExpirationIsMoreThanFiveSeconds() {
        // Given
        String validUrl = "http://valid-url";
        when(props.getRequestUrl()).thenReturn(validUrl);

        AuthToken authToken = new AuthToken("test-token", "bearer", 10, "acr", "scope");
        mockRestClient(validUrl).thenReturn(ResponseEntity.ok(authToken));

        // When
        String accessToken = tokenService.getAccessToken();
        String accessToken2 = tokenService.getAccessToken();

        // Then
        assertEquals("test-token", accessToken);
        assertEquals(accessToken, accessToken2);
        verify(restClient, times(1)).post();
    }

    @Test
    void shouldRefreshAccessTokenIfExpirationIsLessThanFiveSeconds() {
        // Given
        String validUrl = "http://valid-url";
        when(props.getRequestUrl()).thenReturn(validUrl);

        AuthToken initialToken = new AuthToken("initial-token", "bearer", 4, "acr", "scope");
        AuthToken refreshedToken = new AuthToken("refreshed-token", "bearer", 10, "acr", "scope");
        mockRestClient(validUrl)
                .thenReturn(ResponseEntity.ok(initialToken))
                .thenReturn(ResponseEntity.ok(refreshedToken));

        // When
        String accessToken = tokenService.getAccessToken();
        accessToken = tokenService.getAccessToken();

        // Then
        assertEquals("refreshed-token", accessToken);
        verify(restClient, times(2)).post();
    }

    @Test
    void shouldGetBearerToken() {
        // Given
        String validUrl = "http://valid-url";
        when(props.getRequestUrl()).thenReturn(validUrl);

        AuthToken authToken = new AuthToken("test-token", "bearer", 10, "acr", "scope");
        mockRestClient(validUrl).thenReturn(ResponseEntity.ok(authToken));

        // When
        String bearerToken = tokenService.getBearerToken();

        // Then
        assertEquals("Bearer test-token", bearerToken);
    }

    @Test
    void shouldNotRefreshTokenIfStillValid() {
        // Given
        String validUrl = "http://valid-url";
        when(props.getRequestUrl()).thenReturn(validUrl);
        AuthToken authToken = new AuthToken("test-token", "bearer", 10, "acr", "scope");

        mockRestClient(validUrl).thenReturn(ResponseEntity.ok(authToken));

        // When
        tokenService.getAccessToken();
        String accessToken = tokenService.getAccessToken();

        // Then
        assertEquals("test-token", accessToken);
    }

    // Todo sjekk casing og rett mapping av form-data
}
