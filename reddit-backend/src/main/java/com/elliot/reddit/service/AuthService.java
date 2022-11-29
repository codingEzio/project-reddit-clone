package com.elliot.reddit.service;

import com.elliot.reddit.dto.RegisterRequest;
import com.elliot.reddit.exception.SpringRedditException;
import com.elliot.reddit.model.NotificationEmail;
import com.elliot.reddit.model.User;
import com.elliot.reddit.model.VerificationToken;
import com.elliot.reddit.repository.UserRepository;
import com.elliot.reddit.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.UUID;

import static com.elliot.reddit.util.Constants.ACTIVATION_EMAIL;
import static java.time.Instant.now;

@AllArgsConstructor
@Slf4j
@Service
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final VerificationTokenRepository verificationTokenRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	@Transactional
	public void signup(@RequestBody RegisterRequest registerRequest) {
		User user = new User();

		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encodePassword(registerRequest.getPassword()));

		user.setCreated(now());
		user.setEnabled(false);

		userRepository.save(user);
		String token = generateVerificationToken(user);

		String message = mailContentBuilder.build(
				"Thank you for signing up. Click this to activate: "
				+ ACTIVATION_EMAIL
				+ "/"
				+ token
		);

		mailService.sendMail(new NotificationEmail(
				"Activate your account",
				user.getEmail(),
				message
		));
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	private String generateVerificationToken(User user) {
		VerificationToken verificationToken = new VerificationToken();
		String token = UUID.randomUUID().toString();

		verificationToken.setUser(user);
		verificationToken.setToken(token);
		verificationTokenRepository.save(verificationToken);

		return token;
	}
}