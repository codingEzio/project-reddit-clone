package com.elliot.reddit.service;

import com.elliot.reddit.dto.SubredditDto;
import com.elliot.reddit.model.Subreddit;
import com.elliot.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.Instant.now;

@AllArgsConstructor
@Service
public class SubredditService {
	private final SubredditRepository subredditRepository;
	private final AuthService authService;

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));

		subredditDto.setId(subreddit.getId());

		return subredditDto;
	}

	private Subreddit mapToSubreddit(SubredditDto subredditDto) {
		return Subreddit.builder()
				.name("/r/" + subredditDto.getName())
				.description(subredditDto.getDescription())
				.user(authService.getCurrentUser())
				.createdDate(now())
				.build();
	}
}