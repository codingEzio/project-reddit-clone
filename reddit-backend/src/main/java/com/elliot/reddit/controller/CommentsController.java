package com.elliot.reddit.controller;

import com.elliot.reddit.dto.CommentsDto;
import com.elliot.reddit.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@AllArgsConstructor
@RestController
@RequestMapping("/api/comments/")
public class CommentsController {
	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
		commentService.createComment(commentsDto);

		return new ResponseEntity<>(CREATED);
	}

	@GetMapping("by-postid/{postId}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable("postId") Long postId) {
		return status(OK).body(commentService.getCommentsForPost(postId));
	}

	@GetMapping("by-user/{username}")
	public ResponseEntity<List<CommentsDto>> getAllCommentsByUser(@PathVariable("username") String username) {
		return status(OK).body(commentService.getCommentsByUser(username));
	}
}