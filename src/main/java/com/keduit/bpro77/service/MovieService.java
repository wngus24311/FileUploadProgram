package com.keduit.bpro77.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.keduit.bpro77.dto.MovieDTO;
import com.keduit.bpro77.dto.MovieImageDTO;
import com.keduit.bpro77.entity.Member;
import com.keduit.bpro77.entity.Movie;
import com.keduit.bpro77.entity.MovieImage;

public interface MovieService {
	
	Long register(MovieDTO movieDTO);
	
	default Map<String, Object> dtoToEntity(MovieDTO dto) {
		Map<String, Object> entityMap = new HashMap<>();
	
		Movie movie = Movie.builder()
				.mno(dto.getMno())
				.title(dto.getTitle())
				.build();
		
		entityMap.put("movie", movie);
		
		List<MovieImageDTO> imageDTOList = dto.getImageDTOList();
		
		if (imageDTOList != null && imageDTOList.size() > 0) {
			List<MovieImage> movieImageList = imageDTOList.stream().
					map(movieImageDTO -> {
						MovieImage movieImage = MovieImage.builder()
								.path(movieImageDTO.getPath())
								.imageName(movieImageDTO.getImgName())
								.uuid(movieImageDTO.getUuid())
								.movie(movie)
								.build();
						
						return movieImage;
					}).collect(Collectors.toList());
			
			entityMap.put("imgList", movieImageList);
		}
		return entityMap;
	}
}
