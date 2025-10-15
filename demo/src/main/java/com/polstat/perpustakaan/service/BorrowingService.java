package com.polstat.perpustakaan.service;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BorrowingService {

    private final LoanRepository loanRepository;

    public BorrowingService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    // Semua peminjaman
    public List<Loan> getAllBorrowings() {
        return loanRepository.findAll();
    }

    // Peminjaman berdasarkan memberId
    public List<Loan> getBorrowingsByMember(Long memberId) {
        return loanRepository.findByMemberId(memberId);
    }

    // Pinjam buku dengan memberId & bookId
    public Loan borrowBook(Long memberId, Long bookId) {
        Loan loan = Loan.builder()
                .memberId(memberId)
                .bookId(bookId)
                .loanDate(LocalDate.now())
                .returned(false)
                .build();
        return loanRepository.save(loan);
    }

    // Kembalikan buku
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());
        return loanRepository.save(loan);
    }
}
