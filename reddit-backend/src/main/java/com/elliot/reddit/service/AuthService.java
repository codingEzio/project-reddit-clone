package com.elliot.reddit.service;

import com.elliot.reddit.dto.AuthenticationResponse;
import com.elliot.reddit.dto.LoginRequest;
import com.elliot.reddit.dto.RegisterRequest;
import com.elliot.reddit.exception.SpringRedditException;
import com.elliot.reddit.model.NotificationEmail;
import com.elliot.reddit.model.User;
import com.elliot.reddit.model.VerificationToken;
import com.elliot.reddit.repository.UserRepository;
import com.elliot.reddit.repository.VerificationTokenRepository;
import com.elliot.reddit.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	@Transactional
	public void signup(@RequestBody RegisterRequest registerRequest) {
		User user = new User();

		user.setUsername(registerRequest.getUsername());
		user.setEmail(registerRequest.getEmail());
		user.setPassword(encodePassword(registerRequest.getPassword()));

		user.setCreated(now());
		user.setEnabled(false);

		userRepository.save(user);
		log.info("User registered successfully, sending auth email..");

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

	public void verifyAccount(String token) {
		Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);

		verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid token"));

		fetchUserAndEnable(verificationTokenOptional.get());
	}

	public AuthenticationResponse login(LoginRequest loginRequest) {
		Authentication authenticated = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getUsername(),
						loginRequest.getPassword()
				));

		SecurityContextHolder.getContext().setAuthentication(authenticated);
		String authenticationToken = jwtProvider.generateToken(authenticated);

		return new AuthenticationResponse(
				authenticationToken,
				loginRequest.getUsername()
		);
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

	@Transactional
	void fetchUserAndEnable(VerificationToken verificationToken) {
		String username = verificationToken.getUser().getUsername();
		User user = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new SpringRedditException("User not found with id - " + username));

		user.setEnabled(true);

		userRepository.save(user);
	}
}