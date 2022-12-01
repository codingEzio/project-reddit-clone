package com.elliot.reddit.service;

import com.elliot.reddit.dto.CommentsDto;
import com.elliot.reddit.exception.PostNotFoundException;
import com.elliot.reddit.mapper.CommentMapper;
import com.elliot.reddit.model.Comment;
import com.elliot.reddit.model.NotificationEmail;
import com.elliot.reddit.model.Post;
import com.elliot.reddit.model.User;
import com.elliot.reddit.repository.CommentRepository;
import com.elliot.reddit.repository.PostRepository;
import com.elliot.reddit.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Slf4j
@Transactional
@Service
public class CommentService {
	private static final String POST_URL = "";

	private final CommentMapper commentMapper;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;

	public void createComment(CommentsDto commentsDto) {
		Post post = postRepository
				.findById(commentsDto.getPostId())
				.orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));

		Comment comment = commentMapper.map(
				commentsDto,
				post,
				authService.getCurrentUser()
		);
		commentRepository.save(comment);

		String message = mailContentBuilder.build(
				post.getUser().getUsername()
						+ " posted a comment on your post."
						+ POST_URL
		);
		sendCommentNotification(message, post.getUser());
	}

	public List<CommentsDto> getCommentsForPost(Long postId) {
		Post post = postRepository
				.findById(postId)
				.orElseThrow(() -> new PostNotFoundException(postId.toString()));

		return commentRepository
				.findByPost(post)
				.stream()
				.map(commentMapper::mapToDto)
				.collect(toList());
	}

	public List<CommentsDto> getCommentsByUser(String username) {
		User user = userRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));

		return commentRepository
				.findAllByUser(user)
				.stream()
				.map(commentMapper::mapToDto)
				.collect(toList());
	}

	private void sendCommentNotification(String message, User user) {
		mailService.sendMail(new NotificationEmail(
				user.getUsername() + " commented on your post",
				user.getEmail(),
				message
		));
	}
}