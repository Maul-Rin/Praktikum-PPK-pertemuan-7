package com.polstat.perpustakaan.repository;

import com.polstat.perpustakaan.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {}
