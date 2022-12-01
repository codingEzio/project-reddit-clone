package com.elliot.reddit.service;

import com.elliot.reddit.exception.SpringRedditException;
import com.elliot.reddit.model.RefreshToken;
import com.elliot.reddit.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Transactional
@Service
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	RefreshToken generateRefreshToken() {
		RefreshToken refreshToken = new RefreshToken();

		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken.setCreatedDate(Instant.now());

		return refreshTokenRepository.save(refreshToken);
	}

	void validateRefreshToken(String token) {
		refreshTokenRepository
				.findByToken(token)
				.orElseThrow(() -> new SpringRedditException("Invalid refresh token"));
	}

	public void deleteRefreshToken(String token) {
		refreshTokenRepository.deleteByToken(token);
	}
}