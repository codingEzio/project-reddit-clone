package com.elliot.reddit.service;

import com.elliot.reddit.dto.SubredditDto;
import com.elliot.reddit.exception.SpringRedditException;
import com.elliot.reddit.mapper.SubredditMapper;
import com.elliot.reddit.model.Subreddit;
import com.elliot.reddit.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
@Service
public class SubredditService {
	private final SubredditRepository subredditRepository;
	private final SubredditMapper subredditMapper;

	@Transactional(readOnly = true)
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit save = subredditRepository.save(
				subredditMapper.mapDtoToSubreddit(subredditDto)
		);

		subredditDto.setId(save.getId());

		return subredditDto;
	}

	public SubredditDto getSubreddit(Long id) {
		Subreddit subreddit = subredditRepository
				.findById(id)
				.orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));

		return subredditMapper.mapSubredditToDto(subreddit);
	}

	@Transactional(readOnly = true)
	public List<SubredditDto> getAllSubreddit() {
		return subredditRepository.findAll()
				.stream()
				.map(subredditMapper::mapSubredditToDto)
				.collect(toList());
	}
}