package com.elliot.reddit.repository;

import com.elliot.reddit.model.Comment;
import com.elliot.reddit.model.Post;
import com.elliot.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByPost(Post post);

	List<Comment> findAllByUser(User user);
}