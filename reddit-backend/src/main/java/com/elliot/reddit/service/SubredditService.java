package com.elliot.reddit.service;

import com.elliot.reddit.dto.SubredditDto;
import com.elliot.reddit.exception.SubredditNotFoundException;
import com.elliot.reddit.model.Subreddit;
import com.elliot.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class SubredditService {
	private final SubredditRepository subredditRepository;
	private final AuthService authService;

	@Transactional(readOnly = true)
	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository
				.findById(id)
				.orElseThrow(
						() -> new SubredditNotFoundException(
								"Subreddit not found with id - " + id
						)
				);

		return mapToDto(subreddit);
	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAllSubreddit() {
		return subredditRepository.findAll()
				.stream()
				.map(this::mapToDto)
				.collect(toList());
	}

	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = subredditRepository.save(mapToSubreddit(subredditDto));

		subredditDto.setId(subreddit.getId());

		return subredditDto;
	}

	private SubredditDto mapToDto(Subreddit subreddit) {
		return SubredditDto.builder()
				.name(subreddit.getName())
				.id(subreddit.getId())
				.postCount(subreddit.getPosts().size())
				.build();
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