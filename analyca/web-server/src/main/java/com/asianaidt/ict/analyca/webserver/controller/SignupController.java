package com.asianaidt.ict.analyca.webserver.controller;

import com.asianaidt.ict.analyca.domain.securitycore.model.Member;
import com.asianaidt.ict.analyca.service.securityservice.MemberManager;
import com.asianaidt.ict.analyca.webserver.service.HDFSBrowserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/signup")
public class SignupController {
    private final MemberManager memberManager;

    final HDFSBrowserService hdfsBrowserService;

    public SignupController(MemberManager memberManager, HDFSBrowserService hdfsBrowserService) {
        this.memberManager = memberManager;
        this.hdfsBrowserService = hdfsBrowserService;
    }

    @ModelAttribute("member")
    public Member member() {
        return new Member();
    }

    @GetMapping
    public String signupForm(Model model) {
        return "signup";
    }

    @PostMapping
    public String signup(@ModelAttribute("member") @Valid Member member, BindingResult result) {
        member.setPassword(new BCryptPasswordEncoder().encode(member.getPassword()));
        if (!memberManager.signup(member, "USER")) {
            result.rejectValue("username", null, "이미 가입한 사용자입니다.");
        } else {
            hdfsBrowserService.createDirectory("/user/" + member.getUsername(), member.getUsername(), "ROLE_ADMIN");
            hdfsBrowserService.setOwner("/user/" + member.getUsername(), member.getUsername(), "ROLE_ADMIN");
        }

        if (result.hasErrors()) {
            return "signup";
        }
        return "redirect:/";
    }
}
