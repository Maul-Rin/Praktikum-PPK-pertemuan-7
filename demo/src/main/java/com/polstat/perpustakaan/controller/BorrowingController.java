package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Loan;
import com.polstat.perpustakaan.service.BorrowingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @GetMapping
    public List<Loan> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }

    @GetMapping("/member/{memberId}")
    public List<Loan> getBorrowingsByMember(@PathVariable Long memberId) {
        return borrowingService.getBorrowingsByMember(memberId);
    }

    @PostMapping("/borrow/{memberId}/{bookId}")
    public Loan borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        return borrowingService.borrowBook(memberId, bookId);
    }

    @PostMapping("/return/{loanId}")
    public Loan returnBook(@PathVariable Long loanId) {
        return borrowingService.returnBook(loanId);
    }
}
