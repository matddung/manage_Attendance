package com.example.Attendance.member;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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

@RequestMapping("/")
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showUserLogin() {
        return "login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String showSignup() {
        if (memberService.getCurrentUser() == null) {
            return "signup";
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
                signupForm.getBirth(),
                signupForm.getAddress(),
                signupForm.getEmail(),
                signupForm.getDepartment(),
                signupForm.getPosition()
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

        @NotBlank
        private String address;

        @NotBlank
        private String department;

        @NotBlank
        private String position;

        @NotBlank
        @Email
        private String email;
    }


    // admin -----------------------------------------------------------------------------------------------------------
    @GetMapping("/getList")
    public String getMemberList(Model model) {
        if (!memberService.getCurrentUser().isAdmin()) {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }
        List<Member> members = memberService.getMemberList();
        model.addAttribute("members", members);
        return "member_list";
    }
}