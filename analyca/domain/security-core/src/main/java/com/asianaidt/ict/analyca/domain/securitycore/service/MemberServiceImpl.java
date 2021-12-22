package com.asianaidt.ict.analyca.domain.securitycore.service;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.domain.securitycore.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member findByUsername(String username) {
        return this.memberRepository.findByUsername(username);
    }

    @Override
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        // page의 index는 0부터 시작
        int page = pageable.getPageNumber() == 0 ? 0 : pageable.getPageNumber() - 1;
        pageable = PageRequest.of(page, 10);
        return memberRepository.findAll(pageable);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public void deleteByUsername(String username) {
        memberRepository.deleteByUsername(username);
    }

}
