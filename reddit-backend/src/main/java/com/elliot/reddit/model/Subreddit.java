package com.elliot.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subreddit {
	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@OneToMany
	private List<Post> posts;

	@ManyToOne(fetch = LAZY)
	private User user;

	@NotBlank(message = "Name of the subreddit is required")
	private String name;

	@NotBlank(message = "Description is required")
	private String description;

	private Instant createdDate;
}