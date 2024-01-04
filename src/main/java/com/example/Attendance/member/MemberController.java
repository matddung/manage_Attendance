package com.example.Attendance.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public Member doSignup(@RequestParam(name = "memberId") String memberId,
                           @RequestParam(name = "memberPwd") String memberPwd,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "phoneNumber") String phoneNumber,
                           @RequestParam(name = "birth") String birth,
                           @RequestParam(name = "address") String address,
                           @RequestParam(name = "email") String email) {
        return memberService.memberSignup(memberId, memberPwd, name, phoneNumber, birth, address, email);
    }

    // myPage ----------------------------------------------------------------------------------------------------------
    @PatchMapping("/edit/other")
    public Member doEditInformation(@RequestParam(name = "email") String email,
                                    @RequestParam(name = "phoneNumber") String phoneNumber,
                                    @RequestParam(name = "address") String address) {
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
    public Member editPwd(@RequestParam(name = "MemberPwdConfirm") String MemberPwdConfirm,
                           @RequestParam(name = "memberPwd") String memberPwd,
                           @RequestParam(name = "memberPwd2") String memberPwd2) {
        Member isLoginedMember = memberService.getCurrentMember();

        if (isLoginedMember == null) {
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
                                 @RequestParam(name = "department") String department,
                                 @RequestParam(name = "position") String position) {
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
                               @RequestParam(name = "department") String department,
                               @RequestParam(name = "position") String position) {
        if (!memberService.getCurrentMember().isAdmin()) {
            throw new RuntimeException("관리자 권한이 필요합니다.");
        }
        Member member = memberService.findById(id);
        return memberService.editPosition(member, department, position);
    }
}