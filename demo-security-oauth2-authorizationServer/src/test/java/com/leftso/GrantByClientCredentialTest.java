package com.leftso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtContext;
import org.springframework.web.client.ResourceAccessException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GrantByClientCredentialTest extends OAuth2Test {

    @Value("${local.server.port}")
    private int port;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void getJwtTokenByTrustedClient() throws JsonParseException, JsonMappingException, IOException {
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:" + port + "/oauth/token?client_id=trusted-app&grant_type=client_credentials", null, String.class);
        String responseText = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        HashMap jwtMap = new ObjectMapper().readValue(responseText, HashMap.class);

        assertEquals("bearer", jwtMap.get("token_type"));
        assertEquals("read write", jwtMap.get("scope"));
        assertTrue(jwtMap.containsKey("access_token"));
        assertTrue(jwtMap.containsKey("expires_in"));
        assertTrue(jwtMap.containsKey("jti"));
        String accessToken = (String) jwtMap.get("access_token");

        Jwt jwtToken = JwtHelper.decode(accessToken);

        String claims = jwtToken.getClaims();
        logJson(claims);

        HashMap claimsMap = new ObjectMapper().readValue(claims, HashMap.class);
        assertEquals("spring-boot-application", ((List<String>) claimsMap.get("aud")).get(0));
        assertEquals("trusted-app", claimsMap.get("client_id"));
        assertEquals("read", ((List<String>) claimsMap.get("scope")).get(0));
        assertEquals("write", ((List<String>) claimsMap.get("scope")).get(1));
        List<String> authorities = (List<String>) claimsMap.get("authorities");
        assertEquals(1, authorities.size());
        assertEquals("ROLE_TRUSTED_CLIENT", authorities.get(0));
    }

    @Test(expected = ResourceAccessException.class)
    public void accessWithUnknownClientID() throws JsonParseException, JsonMappingException, IOException {
        ResponseEntity<String> response = new TestRestTemplate("trusted-app", "secrets").postForEntity("http://localhost:" + port + "/oauth/token?client_id=trusted-app&grant_type=client_credentials", null, String.class);
    }

    @Test
    public void accessProtectedResourceByJwtToken() throws JsonParseException, JsonMappingException, IOException, InvalidJwtException {
        ResponseEntity<String> response = new TestRestTemplate().getForEntity("http://localhost:" + port + "/resources/client", String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        response = new TestRestTemplate("trusted-app", "secret").postForEntity("http://localhost:" + port + "/oauth/token?client_id=trusted-app&grant_type=client_credentials", null, String.class);
        String responseText = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        HashMap jwtMap = new ObjectMapper().readValue(responseText, HashMap.class);
        String accessToken = (String) jwtMap.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        JwtContext jwtContext = jwtConsumer.process(accessToken);
        logJWTClaims(jwtContext);

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/principal", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals("trusted-app", response.getBody());

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/trusted_client", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        response = new TestRestTemplate().exchange("http://localhost:" + port + "/resources/roles", HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertEquals("[{\"authority\":\"ROLE_TRUSTED_CLIENT\"}]", response.getBody());

    }

}
