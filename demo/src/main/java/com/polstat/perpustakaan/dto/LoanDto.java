package com.polstat.perpustakaan.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanDto {
    private Long id;
    private Long memberId;
    private Long bookId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate borrowDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

    private String status;
    private Long daysLate;
}
