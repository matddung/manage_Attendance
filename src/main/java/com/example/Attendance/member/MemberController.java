package com.example.Attendance.member;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/")
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    // login, logout, signup -------------------------------------------------------------------------------------------
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showMemberLogin() {
        return "login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String showSignup() {
        if (memberService.getCurrentMember() == null) {
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



    // myPage ----------------------------------------------------------------------------------------------------------
    @PostMapping("/checkPassword")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> payload) {
        Member isLoginedMember = memberService.getCurrentMember();
        String rawPwd = payload.get("password");

        return ResponseEntity.ok(memberService.checkPassword(isLoginedMember, rawPwd));
    }

    @GetMapping("/myPage")
    public String myPage(Model model) {
        Member isLoginedMember = memberService.getCurrentMember();

        if(isLoginedMember == null) {
            return "redirect:/";
        }

        model.addAttribute("isLoginedMember", isLoginedMember);

        return "myPage";
    }

    @PostMapping("/edit/other")
    public String doEditInformation(@RequestParam String email, @RequestParam String phoneNumber, @RequestParam String address, Model model) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (email.isEmpty()) {
            email = isLoginedMember.getEmail();
        }

        if (phoneNumber.isEmpty()) {
            phoneNumber = isLoginedMember.getPhoneNumber();
        }

        if (address.isEmpty()) {
            address = isLoginedMember.getPhoneNumber();
        }

        if (isLoginedMember != null) {
            memberService.editInfo(isLoginedMember, email, phoneNumber, address);
            model.addAttribute("isLoginedMember", isLoginedMember);

            return "redirect:/";
        }

        return "redirect:/";
    }

    @PostMapping("/edit/password")
    public String editPwd(@RequestParam String MemberPwdConfirm, @RequestParam String memberPwd, @RequestParam String memberPwd2) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (memberService.checkPassword(isLoginedMember, MemberPwdConfirm)) {
            if (memberPwd.equals(memberPwd2)) {
                memberService.editPwd(isLoginedMember, memberPwd);
                return "redirect:/";
            }
        } else {
            return "redirect:/member/myPage";
        }

        return "redirect:/";
    }



    // admin -----------------------------------------------------------------------------------------------------------
    @GetMapping("/getList")
    public String getMemberList(Model model) {
        if (!memberService.getCurrentMember().isAdmin()) {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }
        List<Member> members = memberService.getMemberList();
        model.addAttribute("members", members);
        return "member_list";
    }

    @PostMapping("/editPosition/{id}")
    public String editPosition(@PathVariable long id, @RequestParam String department, @RequestParam String position) {
        if (!memberService.getCurrentMember().isAdmin()) {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }

        Member member = memberService.findById(id);

        member.setPosition(position);
        member.setDepartment(department);

        return "member_list";
    }
}