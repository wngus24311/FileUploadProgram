package com.keduit.bpro77.repository;

import com.keduit.bpro77.entity.Member;
import com.keduit.bpro77.entity.Movie;
import com.keduit.bpro77.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Entity;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	@EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
	List<Review> findByMovie(Movie movie);
	
	@Modifying
	@Query("DELETE FROM Review mr WHERE mr.member = :member")
	void deleteByMember(Member member);
}
