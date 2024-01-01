package com.example.Attendance.member;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PostMapping("/signup")
    public Member doSignup(@Valid SignupForm signupForm) {
        RsData<Member> signupRs = memberService.memberSignup(
                signupForm.getMemberId(),
                signupForm.getMemberPwd(),
                signupForm.getName(),
                signupForm.getPhoneNumber(),
                signupForm.getBirth(),
                signupForm.getAddress(),
                signupForm.getEmail()
        );

        return signupRs.getData();
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
        @Email
        private String email;
    }


    // myPage ----------------------------------------------------------------------------------------------------------
    @PatchMapping("/edit/other")
    public Member doEditInformation(@Parameter(name = "email") @RequestParam String email,
                                    @Parameter(name = "phoneNumber") @RequestParam String phoneNumber,
                                    @Parameter(name = "address") @RequestParam String address) {
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
            return memberService.editInfo(isLoginedMember, email, phoneNumber, address);
        } else {
            throw new RuntimeException("로그인이 필요합니다.");
        }

    }

    @PatchMapping("/edit/password")
    public Member editPwd(@Parameter(name = "MemberPwdConfirm") @RequestParam String MemberPwdConfirm,
                          @Parameter(name = "memberPwd") @RequestParam String memberPwd,
                          @Parameter(name = "memberPwd2") @RequestParam String memberPwd2) {
        Member isLoginedMember = memberService.getCurrentMember();

        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        if (memberService.checkPassword(isLoginedMember, MemberPwdConfirm)) {
            if (memberPwd.equals(memberPwd2)) {
                return memberService.editPwd(isLoginedMember, memberPwd);
            } else {
                throw new RuntimeException("비밀번호가 서로 일치하지 않습니다.");
            }
        } else {
            throw new RuntimeException("비밀번호를 확인해주세요.");
        }
    }


    // admin -----------------------------------------------------------------------------------------------------------
    @GetMapping("/getList")
    public List<Member> getMemberList() {
        if (!memberService.getCurrentMember().isAdmin()) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        List<Member> members = memberService.getMemberList();
        return members;
    }

    @GetMapping("/getWaitingMemberList")
    public List<Member> getWaitingMemberList() {
        if (!memberService.getCurrentMember().isAdmin()) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        List<Member> waitingMembers = memberService.getWaitingMemberList();
        return waitingMembers;
    }

    @PatchMapping("/approveMember/{id}")
    public Member approveMember(@PathVariable long id,
                                @Parameter(name = "department") @RequestParam String department,
                                @Parameter(name = "position") @RequestParam String position) {
        if (!memberService.getCurrentMember().isAdmin()) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        Member member = memberService.findById(id);
        memberService.approveMember(id);
        memberService.editPosition(member, department, position);
        return member;
    }

    @PatchMapping("/editPosition/{id}")
    public Member editPosition(@PathVariable long id,
                               @Parameter(name = "department") @RequestParam String department,
                               @Parameter(name = "position") @RequestParam String position) {
        if (!memberService.getCurrentMember().isAdmin()) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        Member member = memberService.findById(id);
        return memberService.editPosition(member, department, position);
    }
}