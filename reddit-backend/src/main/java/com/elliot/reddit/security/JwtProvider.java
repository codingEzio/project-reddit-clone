package com.elliot.reddit.security;

import com.elliot.reddit.exception.SpringRedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static com.elliot.reddit.util.Constants.SECURITY_CERT_KEYSTORE_EXT;
import static com.elliot.reddit.util.Constants.SECURITY_CERT_KEYSTORE_ALIAS;
import static com.elliot.reddit.util.Constants.SECURITY_CERT_KEYSTORE_PASSWD;
import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

@Service
public class JwtProvider {
	private KeyStore keyStore;

	@Value("${jwt.expiration.time}")
	private Long jwtExpirationInMillis;

	@PostConstruct
	public void init() {
		try {
			keyStore = KeyStore.getInstance("JKS");

			// The file was stored under the /resources/ folder
			InputStream resourceAsStream = getClass().getResourceAsStream(
					"/" + SECURITY_CERT_KEYSTORE_ALIAS + SECURITY_CERT_KEYSTORE_EXT
			);
			keyStore.load(resourceAsStream, SECURITY_CERT_KEYSTORE_PASSWD.toCharArray());

		} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException exception) {
			throw new SpringRedditException(
					"Exception while loading keystore"
			);
		} catch (IOException exception) {
			throw new SpringRedditException(
					"Exception while trying to find the KeyStore file"
			);
		}
	}

	public String generateToken(Authentication authentication) {
		User principle = (User) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(principle.getUsername())
				.signWith(getPrivateKey())
				.setExpiration(Date.from(
						Instant.now().plusMillis(jwtExpirationInMillis)
				))
				.compact();
	}

	public String generateTokenWithUsername(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(from(Instant.now()))
				.signWith(getPrivateKey())
				.setExpiration(Date.from(
						Instant.now().plusMillis(jwtExpirationInMillis)
				))
				.compact();
	}

	public boolean validateToken(String jwtToken) {
		parser()
				.setSigningKey(getPublicKey())
				.parseClaimsJws(jwtToken);

		return true;
	}

	public String getUsernameFromJWT(String jwtToken) {
		Claims claims = parser()
				.setSigningKey(getPublicKey())
				.parseClaimsJws(jwtToken)
				.getBody();

		return claims.getSubject();
	}

	public Long getJwtExpirationInMillis() {
		return jwtExpirationInMillis;
	}

	private PrivateKey getPrivateKey() {
		try {
			return (PrivateKey) keyStore.getKey(
					SECURITY_CERT_KEYSTORE_ALIAS,
					SECURITY_CERT_KEYSTORE_PASSWD.toCharArray()
			);
		} catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException exception) {
			throw new SpringRedditException(
					"Exception while retrieving public key from keystore"
			);
		}
	}

	private PublicKey getPublicKey() {
		try {
			return keyStore
					.getCertificate(SECURITY_CERT_KEYSTORE_ALIAS)
					.getPublicKey();
		} catch (KeyStoreException exception) {
			throw new SpringRedditException(
					"Exception while retrieving public key from keystore"
			);
		}
	}
}