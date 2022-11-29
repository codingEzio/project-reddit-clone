package com.elliot.reddit.security;

import com.elliot.reddit.exception.SpringRedditException;
import io.jsonwebtoken.Jwts;
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
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import static com.elliot.reddit.util.Constants.SECURITY_CERT_KEYSTORE_EXT;
import static com.elliot.reddit.util.Constants.SECURITY_CERT_KEYSTORE_ALIAS;
import static com.elliot.reddit.util.Constants.SECURITY_CERT_KEYSTORE_PASSWD;

@Service
public class JwtProvider {
	private KeyStore keyStore;

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
				.compact();
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
}