package com.example.Attendance;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private Rq rq;

    @GetMapping("/changePwd")
    public String changePwd(@RequestParam String name, @RequestParam String memberId) {
        Member member = memberService.findByNameAndMemberId(name, memberId);

        if(member != null) {
            return "changePwd";
        } else {
            return rq.redirect("redirect:/main", "일치하는 회원 정보가 없습니다.");
        }
    }
}