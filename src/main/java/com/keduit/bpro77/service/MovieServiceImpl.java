package com.keduit.bpro77.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.keduit.bpro77.dto.MovieDTO;
import com.keduit.bpro77.entity.Movie;
import com.keduit.bpro77.entity.MovieImage;
import com.keduit.bpro77.repository.MovieImageRepository;
import com.keduit.bpro77.repository.MovieRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
	
	private final MovieRepository movieRepository;
	private final MovieImageRepository movieImageRepository;
	
	@Override
	public Long register(MovieDTO movieDTO) {	
		Map<String, Object> entityMap = dtoToEntity(movieDTO);
		Movie movie = (Movie)entityMap.get("movie");
		List<MovieImage> movieImageList = (List<MovieImage>) entityMap.get("imgList");
		
		movieRepository.save(movie);
		
		movieImageList.forEach(movieImage ->{
			movieImageRepository.save(movieImage);
		});
		
		return movie.getMno();
	}

}
