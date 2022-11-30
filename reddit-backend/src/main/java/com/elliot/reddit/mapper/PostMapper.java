package com.elliot.reddit.mapper;

import com.elliot.reddit.dto.PostRequest;
import com.elliot.reddit.dto.PostResponse;
import com.elliot.reddit.model.Post;
import com.elliot.reddit.model.Subreddit;
import com.elliot.reddit.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
	@Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
	@Mapping(target = "subreddit", source = "subreddit")
	@Mapping(target = "user", source = "user")
	@Mapping(target = "description", source = "postRequest.description")
	Post map(PostRequest postRequest, Subreddit subreddit, User user);

	@Mapping(target = "id", source = "postId")
	@Mapping(target = "postName", source = "postName")
	@Mapping(target = "description", source = "description")
	@Mapping(target = "url", source = "url")
	@Mapping(target = "subredditName", source = "subreddit.name")
	@Mapping(target = "username", source = "user.username")
	PostResponse mapToDto(Post post);
}