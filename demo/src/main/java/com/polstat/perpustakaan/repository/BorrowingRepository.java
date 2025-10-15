package com.polstat.perpustakaan.repository;

import com.polstat.perpustakaan.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    List<Borrowing> findByMemberId(Long memberId);
}
