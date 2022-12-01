package com.elliot.reddit.service;

import com.elliot.reddit.dto.VoteDto;
import com.elliot.reddit.exception.PostNotFoundException;
import com.elliot.reddit.exception.SpringRedditException;
import com.elliot.reddit.model.Post;
import com.elliot.reddit.model.Vote;
import com.elliot.reddit.repository.PostRepository;
import com.elliot.reddit.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.elliot.reddit.model.VoteType.UPVOTE;

@AllArgsConstructor
@Service
public class VoteService {
	private final VoteRepository voteRepository;
	private final PostRepository postRepository;
	private final AuthService authService;

	@Transactional
	public void vote(VoteDto voteDto) {
		// Check the (postId) if it (Post) does exist
		Post post = postRepository
				.findById(voteDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException("Post not found with ID - " + voteDto.getPostId()));

		// Grab the post and the user who did the vote
		Optional<Vote> voteByPostAndUser = voteRepository
				.findTopByPostAndUserOrderByVoteIdDesc(
						post,
						authService.getCurrentUser()
				);

		// If someone who already did the vote wants to upvote or downvote
		if (voteByPostAndUser.isPresent()
				&& voteByPostAndUser.get().getVoteType().equals(voteDto.getVoteType())) {
			throw new SpringRedditException(
					"You have already " + voteDto.getVoteType() + "'d for this post"
			);
		}

		// Either increment or decrement the default value which is initialized to 0
		if (UPVOTE.equals(voteDto.getVoteType())) {
			post.setVoteCount(post.getVoteCount() + 1);
		} else {
			post.setVoteCount(post.getVoteCount() - 1);
		}

		// Save relevant info to both tables
		voteRepository.save(mapToVote(voteDto, post));
		postRepository.save(post);
	}

	private Vote mapToVote(VoteDto voteDto, Post post) {
		return Vote.builder()
				.voteType(voteDto.getVoteType())
				.post(post)
				.user(authService.getCurrentUser())
				.build();
	}
}