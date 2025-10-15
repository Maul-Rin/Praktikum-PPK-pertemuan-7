package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Member;
import com.polstat.perpustakaan.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
// IMPORTS BARU
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Tambahkan ini jika diperlukan

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member (REST)", description = "Manajemen data anggota perpustakaan")
public class MemberController {

    private final MemberRepository memberRepository;

    @Operation(summary = "Mendapatkan semua data anggota perpustakaan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar anggota.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Member.class))))
    })
    @GetMapping
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Operation(summary = "Menambahkan anggota baru ke perpustakaan.")
    @RequestBody(
            description = "Data anggota yang akan ditambahkan (MemberID, Name, Address, dll.)",
            required = true,
            content = @Content(schema = @Schema(implementation = Member.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Berhasil menambahkan anggota.",
                    content = @Content(schema = @Schema(implementation = Member.class))),
            @ApiResponse(responseCode = "400", description = "Input data tidak valid.")
    })
    @PostMapping
    public Member addMember(@org.springframework.web.bind.annotation.RequestBody Member member) {
        return memberRepository.save(member);
    }
}