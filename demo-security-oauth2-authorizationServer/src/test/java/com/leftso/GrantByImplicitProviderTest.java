package com.leftso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GrantByImplicitProviderTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void getJwtTokenByImplicitGrant() throws JsonParseException, JsonMappingException, IOException {
        String redirectUrl = "http://localhost:"+port+"/resources/user";
        ResponseEntity<String> response = new TestRestTemplate("user","password").postForEntity("http://localhost:" + port 
           + "oauth/authorize?response_type=token&client_id=normal-app&redirect_uri={redirectUrl}", null, String.class,redirectUrl);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> setCookie = response.getHeaders().get("Set-Cookie");
        String jSessionIdCookie = setCookie.get(0);
        String cookieValue = jSessionIdCookie.split(";")[0];

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookieValue);
        response = new TestRestTemplate("user","password").postForEntity("http://localhost:" + port 
                + "oauth/authorize?response_type=token&client_id=normal-app&redirect_uri={redirectUrl}&user_oauth_approval=true&authorize=Authorize",
                new HttpEntity<>(headers), String.class, redirectUrl);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertNull(response.getBody());
        String location = response.getHeaders().get("Location").get(0);

        //FIXME: Is this a bug with redirect URL?
        location = location.replace("#", "?");

        response = new TestRestTemplate().getForEntity(location, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
