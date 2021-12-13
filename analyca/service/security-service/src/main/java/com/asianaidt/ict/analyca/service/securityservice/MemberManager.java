package com.asianaidt.ict.analyca.service.securityservice;

import com.asianaidt.ict.analyca.domain.securitycore.model.Authority;
import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.domain.securitycore.model.Role;
import com.asianaidt.ict.analyca.domain.securitycore.service.MemberService;
import com.asianaidt.ict.analyca.domain.securitycore.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MemberManager {
    private final MemberService memberService;
    private final RoleService roleService;

    public MemberManager(MemberService memberService, RoleService roleService) {
        this.memberService = memberService;
        this.roleService = roleService;
    }

    public boolean signup(final Member member, final String roleName) {
        Member existing = memberService.findByUsername(member.getUsername());
        if (existing != null) return false;

        Role role = roleService.findByName(roleName);
        if (role == null) return false;

        Authority authority = new Authority();
        authority.setRole(role);
        member.addAuthority(authority);
        memberService.save(member);
        return true;
    }

    public Page<Member> findAllWithPaging(Pageable pageable) {
        return memberService.findAll(pageable);
    }

    public List<Member> findAllMember() {
        return memberService.findAll();
    }

    public List<Role> findAllRole() {
        return roleService.findAll();
    }

    /**
     * 사용자 정보를 업데이트 한다.
     * 현재는 사용자 권한만 업데이트를 진행하고 있으며 사용자의 이름과 비밀번호는 항상 제외이다.
     * 업데이트 대상은 기존 데이터와 비교 후 변경됐을 때만 업데이트를 하도록 한다.
     * <p>
     * 주의
     * 1. 현재 모든 멤버는 권한을 1개 가지고 있기 때문에 코드상으로 인덱스를 직접 접근하고 있음.
     *
     * @param updateMember 업데이트 대상의 정보가 담겨있으며 아래의 키 값으로 값을 얻어올 수 있다.
     *                     username - 사용자 이름
     *                     password - 사용자 비밀번호
     *                     role - 사용자 권한
     */
    public void updateMember(HashMap<String, String> updateMember) {
        String updateUsername = updateMember.get("username");
        String updateRolename = updateMember.get("role");
        Member findMember = memberService.findByUsername(updateUsername);
        if (findMember.getRole().equals(updateRolename)) return;

        Role findRole = roleService.findByName(updateRolename);
        /* 주의 1 */
        findMember.getAuthorites().get(0).setRole(findRole);
        memberService.save(findMember);
    }

    public void deleteMember(HashMap<String, String> deleteMember) {
        memberService.deleteByUsername(deleteMember.get("username"));
    }
}