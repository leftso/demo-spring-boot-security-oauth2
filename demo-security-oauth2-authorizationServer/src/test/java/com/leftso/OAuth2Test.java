package com.leftso;

import org.jose4j.json.JsonUtil;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author thomas.tesche
 */
public class OAuth2Test {

	protected JwtConsumer jwtConsumer;

	protected static final Logger logger = LoggerFactory.getLogger(OAuth2Test.class);

	@Before
	public void setup() {
		jwtConsumer = new JwtConsumerBuilder().setSkipAllValidators().setDisableRequireSignature()
				.setSkipSignatureVerification().build();
	}

	protected void logJWTClaims(JwtContext jwtContext) {
		logger.info(prettyPrintJson(JsonUtil.toJson(jwtContext.getJwtClaims().getClaimsMap())));
	}

	protected void logJson(String json) {
		logger.info(prettyPrintJson(json));
	}

	protected String prettyPrintJson(String flatJson) {
		try {
			return (new JSONObject(flatJson).toString(3));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
