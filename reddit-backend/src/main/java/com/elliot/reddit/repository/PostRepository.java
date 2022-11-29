package com.elliot.reddit.repository;

import com.elliot.reddit.model.Post;
import com.elliot.reddit.model.Subreddit;
import com.elliot.reddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findAllBySubreddit(Subreddit subreddit);

	List<Post> findByUser(User user);
}