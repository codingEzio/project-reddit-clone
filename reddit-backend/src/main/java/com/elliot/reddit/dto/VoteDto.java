package com.elliot.reddit.dto;

import com.elliot.reddit.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
	private Long postId;
	private VoteType voteType;
}