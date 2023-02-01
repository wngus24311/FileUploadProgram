package com.keduit.bpro77.repository;

import com.keduit.bpro77.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
