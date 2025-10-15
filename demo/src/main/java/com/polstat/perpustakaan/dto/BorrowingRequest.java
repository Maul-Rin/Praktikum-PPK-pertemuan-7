package com.polstat.perpustakaan.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRequest {
    private Long memberId;
    private Long bookId;
}
