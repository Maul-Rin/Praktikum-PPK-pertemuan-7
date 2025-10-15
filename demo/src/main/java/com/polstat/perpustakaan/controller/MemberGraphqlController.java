package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Member;
import com.polstat.perpustakaan.repository.MemberRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping; // Import ini mungkin tidak digunakan
import org.springframework.web.bind.annotation.RequestMapping; // Import ini mungkin tidak digunakan

import java.util.List;
import java.util.Optional;

@Controller
public class MemberGraphqlController {

    private final MemberRepository memberRepository;

    // FIX: Gunakan Constructor Injection
    public MemberGraphqlController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // QUERY: Mendapatkan semua anggota
    @QueryMapping
    public List<Member> members() {
        return memberRepository.findAll();
    }

    // QUERY: Mendapatkan anggota berdasarkan ID
    @QueryMapping
    public Optional<Member> memberById(@Argument Long id) {
        return memberRepository.findById(id);
    }

    // MUTATION: Menambahkan anggota baru
    @MutationMapping
    public Member addMember(@Argument String name, @Argument String address) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(address);
        return memberRepository.save(member);
    }
}