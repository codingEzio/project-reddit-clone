package com.elliot.reddit.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
	public static final String ACTIVATION_EMAIL =
			"http://localhost"
					+ ":8080"
					+ "/api/auth"
					+ "/accountVerification";
	public static final String EMAIL_SENDER_ADDR = "admin@redditclone.com";

	public static final String SECURITY_CERT_KEYSTORE_EXT = ".jks";
	public static final String SECURITY_CERT_KEYSTORE_ALIAS = "springreddit";
	public static final String SECURITY_CERT_KEYSTORE_PASSWD = "password";
}