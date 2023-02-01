package com.keduit.bpro77.repository;

import com.keduit.bpro77.entity.Member;
import com.keduit.bpro77.entity.Movie;
import com.keduit.bpro77.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {

    @Autowired
    private ReviewRepository reviewRepository;



    @Test
    public void insertMoviewReviews() {

        //200개의 리뷰를 등록
        IntStream.rangeClosed(1,200).forEach(i -> {

            //영화 번호
            Long mno = (long)(Math.random()*100) + 1;

            //리뷰어 번호
            Long mid  =  ((long)(Math.random()*100) + 1 );
            
            Member member = Member.builder().mid(mid).build();

            Review movieReview = Review.builder()
                    .member(member)
                    .movie(Movie.builder().mno(mno).build())
                    .grade((int)(Math.random()* 5) + 1)
                    .text("이 영화에 대한 느낌..."+i)
                    .build();

            reviewRepository.save(movieReview);
        });
    }
    

	@Test
	public void testGetMovieReviews() {
		Movie movie = Movie.builder().mno(12L).build();
		
		List<Review> result = reviewRepository.findByMovie(movie);
		
		result.forEach(value -> {
			System.out.println(value.getReviewnum());
			System.out.println(value.getGrade());
			System.out.println(value.getText());
			System.out.println(value.getMember().getEmail());
		});
		
	}
	


   

}
