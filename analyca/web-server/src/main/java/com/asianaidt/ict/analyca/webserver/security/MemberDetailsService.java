package com.asianaidt.ict.analyca.webserver.security;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.domain.securitycore.service.MemberService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class MemberDetailsService implements UserDetailsService {
    private static final String defaultRolePrefix = "ROLE_";
    private final MemberService memberService;

    public MemberDetailsService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findByUsername(username);
        if (member == null) throw new UsernameNotFoundException(username + "이 없습니다");
        return new User(member.getUsername(), member.getPassword(), getGrantedAuthority(member));
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthority(Member member) {
        return member.getAuthorites().stream()
                .map(a -> new SimpleGrantedAuthority(defaultRolePrefix + a.getRole().getName()))
                .collect(Collectors.toList());
    }
}
