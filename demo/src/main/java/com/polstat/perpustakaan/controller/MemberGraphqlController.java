package com.polstat.perpustakaan.controller;

import com.polstat.perpustakaan.entity.Member;
import com.polstat.perpustakaan.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberGraphqlController {

    @Autowired
    private MemberRepository memberRepository;

    // === QUERY ===

    @QueryMapping
    public List<Member> members() {
        return memberRepository.findAll();
    }

    @QueryMapping
    public Member memberById(@Argument Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElse(null);
    }

    // === MUTATION ===

    @MutationMapping
    public Member createMember(@Argument String memberID,
                               @Argument String name,
                               @Argument String address,
                               @Argument String phoneNumber,
                               @Argument String email) {
        Member member = Member.builder()
                .memberID(memberID)
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .email(email)
                .build();
        return memberRepository.save(member);
    }

    @MutationMapping
    public Member updateMember(@Argument Long id,
                               @Argument String memberID,
                               @Argument String name,
                               @Argument String address,
                               @Argument String phoneNumber,
                               @Argument String email) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setMemberID(memberID);
            member.setName(name);
            member.setAddress(address);
            member.setPhoneNumber(phoneNumber);
            member.setEmail(email);
            return memberRepository.save(member);
        }
        return null;
    }

    @MutationMapping
    public Member deleteMember(@Argument Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            memberRepository.delete(member);
            return member;
        }
        return null;
    }
}
