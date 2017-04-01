package com.leftso;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class OtherTest {
	public static void main(String[] args) {
		try {
			String access_token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsic3ByaW5nLWJvb3QtYXBwbGljYXRpb24iXSwidXNlcl9uYW1lIjoiYXBwX2NsaWVudCIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE0OTA5NTI5NzcsImF1dGhvcml0aWVzIjpbIlJPTEVfVVNFUiJdLCJqdGkiOiJmOTA4Njk5Mi1mNTc5LTQzZmEtODBjYi00MmViOTEzMjJmMTkiLCJjbGllbnRfaWQiOiJub3JtYWwtYXBwIn0.dTXMB_yDMEQ5n5fS9VXKjvPDOMJQIFJwbpP7WQdw4WU";
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + access_token);
			ResponseEntity<String> response = new TestRestTemplate().exchange("http://localhost:" + 8080 + "/resources/roles", HttpMethod.GET,
					new HttpEntity<>(null, headers), String.class);
			System.out.println(response.getBody());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
