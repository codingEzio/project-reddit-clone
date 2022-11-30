package com.elliot.reddit.controller;

import com.elliot.reddit.dto.SubredditDto;
import com.elliot.reddit.service.SubredditService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/subreddit")
public class SubredditController {
	private final SubredditService subredditService;

	@GetMapping("/{id}")
	public SubredditDto getSubreddit(@PathVariable Long id) {
		return subredditService.getSubreddit(id);
	}

	@GetMapping
	public List<SubredditDto> getAllSubreddits() {
		return subredditService.getAllSubreddit();
	}

	@PostMapping
	public SubredditDto create(@RequestBody @Valid SubredditDto subredditDto) {
		return subredditService.save(subredditDto);
	}
}