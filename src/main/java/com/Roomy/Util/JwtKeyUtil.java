package com.Roomy.Util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.Roomy.domain.SourceKeyRing;
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