package com.example.Attendance;

import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/")
    public String Home() {
        return "login";
    }

    @GetMapping("/main")
    public String Main() {
        Member isLoginedMember = memberService.getCurrentMember();

        if(isLoginedMember == null) {
            return "redirect:/";
        }

        return "main";
    }
}