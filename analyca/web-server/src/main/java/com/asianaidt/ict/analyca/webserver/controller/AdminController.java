package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.domain.securitycore.model.Role;
import com.asianaidt.ict.analyca.service.securityservice.MemberManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MemberManager memberManager;

    public AdminController(MemberManager memberManager) {
        this.memberManager = memberManager;
    }

    @GetMapping("/members")
    public String members(@PageableDefault Pageable pageable, Model model) {
        model.addAttribute("members", memberManager.findAllWithPaging(pageable));
        return "admin/members";
    }

    @PostMapping("/members")
    public @ResponseBody
    List<Map<String, String>> members() {
        List<Member> findAll = memberManager.findAllMember();
        return findAll.stream().map(m -> {
            Map<String, String> map = new HashMap<>();
            map.put("username", m.getUsername());
            map.put("password", m.getPassword());
            map.put("role", m.getRole());
            return map;
        }).collect(Collectors.toList());
    }

    @PostMapping("/roles")
    public @ResponseBody
    List<Role> roles() {
        return memberManager.findAllRole();
    }

    /**
     * 사용자 업데이트.
     * 업데이트 승인 버튼을 누르면 해당 함수를 호출하기 때문에 무분별한 업데이트를 막기 위해
     * 업데이트 대상에 대한 조회를 통해 비교 후 업데이트를 한다.
     *
     * @param member 업데이트 대상의 정보가 담겨있으며 아래의 키 값으로 값을 얻어올 수 있다.
     *               username - 사용자 이름
     *               password - 사용자 비밀번호
     *               role - 사용자 권한
     */
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/member")
    public void updateMember(@RequestBody HashMap<String, String> member) {
        memberManager.updateMember(member);
    }

    /**
     * 사용자 삭제.
     *
     * @param member 삭제할 대상의 정보가 담겨있으며 아래의 키 값으로 값을 얻어올 수 있다.
     *               username - 사용자 이름
     *               password - 사용자 비밀번호
     *               role - 사용자 권한
     */
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/member")
    public void deleteMember(@RequestBody HashMap<String, String> member) {
        memberManager.deleteMember(member);
    }
}