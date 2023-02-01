package com.keduit.bpro77.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "movie")
public class MovieImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long inum;
	
	private String uuid;
	
	private String imageName;
	
	private String path;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Movie movie;
}
