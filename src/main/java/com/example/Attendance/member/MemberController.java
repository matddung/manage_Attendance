package com.example.Attendance.member;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showUserLogin() {
        return "member/login_form";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String showSignup() {
        if (memberService.getCurrentUser() == null) {
            return "member/signup_form";
        } else {
            return "redirect:/";
        }
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/signup")
    public String doSignup(@Valid SignupForm signupForm) {
        RsData<Member> signupRs = memberService.memberSignup(
                signupForm.getMemberId(),
                signupForm.getMemberPwd(),
                signupForm.getName(),
                signupForm.getPhoneNumber(),
                signupForm.getBirth()
        );

        if (signupRs.isFail()) {
            rq.historyBack(signupRs.getMsg());
            return "common/js";
        }

        return rq.redirect("/", signupRs.getMsg());
    }

    @Getter
    @AllArgsConstructor
    public static class SignupForm {
        @NotBlank
        private String memberId;

        @NotBlank
        private String memberPwd;

        @NotBlank
        private String name;

        @NotBlank
        private String phoneNumber;

        @NotBlank
        private String birth;
    }



    @GetMapping("/waitLawyerList")
    public String getMemberList(Model model) {
        if (!memberService.getCurrentUser().isAdmin()) {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }
        List<Member> members = memberService.getMemberList();
        model.addAttribute("members", members);
        return "/member_list";
    }
}