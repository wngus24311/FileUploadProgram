package com.keduit.bpro77.repository;

import com.keduit.bpro77.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
	
	@Query("SELECT m, mi, AVG(COALESCE(r.grade,0)), count(DISTINCT r) FROM Movie m"
			+ " LEFT OUTER JOIN MovieImage mi on mi.movie = m"
			+ " LEFT OUTER JOIN "
			+ "Review r on r.movie = m GROUP BY m")
	Page<Object[]> getListPage(Pageable pageable);
	
	@Query("select m, mi, avg(coalesce(r.grade,0)), count(r) "
			+" from Movie m left outer join MovieImage mi on mi.movie = m "
			+" left outer join Review r on r.movie = m "
			+" where m.mno = :mno ")
	List<Object[]> getMovieWithAll(Long mno);
}
