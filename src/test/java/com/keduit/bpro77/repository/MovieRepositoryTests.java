package com.keduit.bpro77.repository;

import com.keduit.bpro77.entity.Movie;
import com.keduit.bpro77.entity.MovieImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MovieRepositoryTests {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private MovieImageRepository movieImageRepository;
	
	@Test
	@Transactional
	@Commit
	public void insertMovies() {
		
		IntStream.rangeClosed(0, 100).forEach(i -> {
			
			Movie movie  = Movie.builder().title("Movie....." + i).build();
			
			System.out.println("------------------------");
			
			movieRepository.save(movie);
			
			int count = (int)(Math.random() * 5) + 1;
			
			for(int j = 0 ; j < count; j++) {
				MovieImage movieImage = MovieImage.builder()
						.uuid(UUID.randomUUID().toString())
						.movie(movie)
						.imageName("test img"+j+".jpg").build();
				
				movieImageRepository.save(movieImage);
			}
		});
	}
	
	@Test
	public void testListPage() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "mno"));
		
		Page<Object[]> result = movieRepository.getListPage(pageRequest);
		
		for (Object[] objs : result.getContent()) {
			System.out.println(Arrays.toString(objs));
		}
	}
	
	@Test
	public void testGetMovieWithAll() {
		List<Object[]> result = movieRepository.getMovieWithAll(12L);
		
		for(Object[] arr : result) {
			System.out.println(Arrays.toString(arr));
		}
	}
	


}
