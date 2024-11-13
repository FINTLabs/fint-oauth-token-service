package no.fint.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OAuthTokenPropsTest {

    private OAuthTokenProps oAuthTokenProps;
    private Resource mockResource;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        oAuthTokenProps = new OAuthTokenProps();
        mockResource = Mockito.mock(Resource.class);

        // Use reflection to set the private field jsonConfiguration
        java.lang.reflect.Field field = OAuthTokenProps.class.getDeclaredField("jsonConfiguration");
        field.setAccessible(true);
        field.set(oAuthTokenProps, mockResource);
    }

    @Test
    void shouldInjectValuesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        // Use reflection to set private fields
        setField(oAuthTokenProps, "username", "testUser");
        setField(oAuthTokenProps, "password", "testPassword");
        setField(oAuthTokenProps, "accessTokenUri", "http://localhost/token");
        setField(oAuthTokenProps, "clientId", "clientId");
        setField(oAuthTokenProps, "clientSecret", "clientSecret");
        setField(oAuthTokenProps, "scope", "read write");
        setField(oAuthTokenProps, "grantType", "password");

        // Assertions
        assertEquals("testUser", oAuthTokenProps.getUsername());
        assertEquals("testPassword", oAuthTokenProps.getPassword());
        assertEquals("http://localhost/token", oAuthTokenProps.getAccessTokenUri());
        assertEquals("clientId", oAuthTokenProps.getClientId());
        assertEquals("clientSecret", oAuthTokenProps.getClientSecret());
        assertEquals("read write", oAuthTokenProps.getScope());
        assertEquals("password", oAuthTokenProps.getGrantType());
    }

    @Test
    void shouldUpdateFromJsonConfiguration() throws IOException {
        // Given: JSON data for updating properties
        String json = """
                {
                    "username": "updatedUser",
                    "password": "updatedPassword",
                    "idpUri": "http://localhost/updatedToken",
                    "clientId": "updatedClientId",
                    "openIdSecret": "updatedClientSecret",
                    "scope": "updatedScope",
                    "grantType": "client_credentials"
                }
                """;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes());

        // Mock resource to return input stream
        when(mockResource.isReadable()).thenReturn(true);
        when(mockResource.getInputStream()).thenReturn(inputStream);

        // When: init() is called
        oAuthTokenProps.init();

        // Then: Values should be updated from JSON
        assertEquals("updatedUser", oAuthTokenProps.getUsername());
        assertEquals("updatedPassword", oAuthTokenProps.getPassword());
        assertEquals("http://localhost/updatedToken", oAuthTokenProps.getAccessTokenUri());
        assertEquals("updatedClientId", oAuthTokenProps.getClientId());
        assertEquals("updatedClientSecret", oAuthTokenProps.getClientSecret());
        assertEquals("updatedScope", oAuthTokenProps.getScope());
        assertEquals("client_credentials", oAuthTokenProps.getGrantType());
    }

    @Test
    void shouldNotThrowExceptionIfJsonConfigurationIsNotReadable() throws IOException {
        // Given: Non-readable resource
        when(mockResource.isReadable()).thenReturn(false);

        // When & Then: No exception should be thrown during init()
        assertDoesNotThrow(() -> oAuthTokenProps.init());
    }

    @Test
    void shouldRespectJsonIgnore() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Use reflection to set private fields
        setField(oAuthTokenProps, "username", "testUser");
        setField(oAuthTokenProps, "requestUrl", "http://localhost/request");

        // Given: ObjectMapper to test serialization
        ObjectMapper objectMapper = new ObjectMapper();

        // When: Serializing to JSON
        String json = objectMapper.writeValueAsString(oAuthTokenProps);

        // Then: `requestUrl` should not be present in the JSON
        assertFalse(json.contains("requestUrl"));
        assertTrue(json.contains("testUser")); // Validate other fields are serialized correctly
    }

    private void setField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
