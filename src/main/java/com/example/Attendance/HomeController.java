package com.example.Attendance;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private Rq rq;

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

    @GetMapping
    public String changePwd(@RequestParam String name, @RequestParam String memberId) {
        Member member = memberService.findByNameAndMemberId(name, memberId);

        if(member != null) {
            return "changePwd";
        } else {
            return rq.redirect("redirect:/", "일치하는 회원 정보가 없습니다.");
        }
    }
}