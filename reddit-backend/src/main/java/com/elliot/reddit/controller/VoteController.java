package com.elliot.reddit.controller;

import com.elliot.reddit.dto.VoteDto;
import com.elliot.reddit.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@AllArgsConstructor
@RestController
@RequestMapping("/api/votes/")
public class VoteController {
	private final VoteService voteService;

	@PostMapping
	public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
		voteService.vote(voteDto);

		return new ResponseEntity<>(OK);
	}
}