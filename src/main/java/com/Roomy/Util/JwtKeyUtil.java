package com.Roomy.Util;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.Roomy.domain.SourceKeyRing;
import com.Roomy.domain.TokenJsonObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtKeyUtil {

	private static final String AUDIENCE = "RoomyClinetApps";

	private static final String ISSUER = "Roomy";

	private static final String SIGNING_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

	/*public static void main(String[] args) throws JOSEException,
			ParseException, JsonParseException, JsonMappingException,
			IOException, net.minidev.json.parser.ParseException {
		SourceKeyRing sourceKeyRing = new SourceKeyRing();
		sourceKeyRing.setOtp(123);

		String token = createJWT(sourceKeyRing);
		Object jsonInString = (Object) decryptToken(token);
		System.out.println(jsonInString);
		ObjectMapper mapper = new ObjectMapper();

		JWTClaimsSet claims = (JWTClaimsSet) decryptToken(token);
		Object clms = claims.getClaim("sourceKeyRing");
		if (clms != null) {
			SourceKeyRing sourceKeyRing1 = mapper.readValue(
					(mapper.writer().writeValueAsString(clms)),
					SourceKeyRing.class);
			System.out.println(sourceKeyRing1.getOtp());
		}

		
		 * TokenJsonObject jobj = (TokenJsonObject) mapper.readValue(
		 * jsonInString.toString(), TokenJsonObject.class);
		 * 
		 * System.out.println(jobj.getSourceKeyRing().getOtp());
		 
	}*/

	public static String createJWT(SourceKeyRing sourceKeyRing)
			throws JOSEException {
		JWSSigner signer = new MACSigner(SIGNING_KEY);
		Date issueTime = new Date();
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().issuer(ISSUER)
				.issueTime(issueTime)
				.audience(new ArrayList<String>(Arrays.asList(AUDIENCE)))
				.subject("AAAA").claim("sourceKeyRing", sourceKeyRing).build();

		JWSHeader myHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).type(
				new JOSEObjectType("JWT")).build();
		SignedJWT signedJwt = new SignedJWT(myHeader, claimsSet);
		signedJwt.sign(signer);
		String jwt = signedJwt.serialize();
		jwt.replace("\\+", "-");
		jwt.replace("\\\\", "_");
		System.out.println("*********************************" + jwt);
		return jwt;

	}

	public static Object decryptToken(String token) throws ParseException,
			JOSEException {
		JWSVerifier verifier = new MACVerifier(SIGNING_KEY);
		SignedJWT signedJwt = SignedJWT.parse(token);
		if (signedJwt.verify(verifier)) {
			return signedJwt.getJWTClaimsSet();
		}
		return "";
	}
}