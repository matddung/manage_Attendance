package com.example.Attendance.member;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> doSignup(@Valid SignupForm signupForm) {
        Map<String, Object> response = new HashMap<>();
        RsData<Member> signupRs = memberService.memberSignup(
                signupForm.getMemberId(),
                signupForm.getMemberPwd(),
                signupForm.getName(),
                signupForm.getPhoneNumber(),
                signupForm.getBirth(),
                signupForm.getAddress(),
                signupForm.getEmail()
        );

        if (signupRs.isFail()) {
            response.put("message", signupRs.getMsg());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("message", signupRs.getMsg());
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    @PostMapping("/checkPassword")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> payload) {
        Member isLoginedMember = memberService.getCurrentMember();
        String rawPwd = payload.get("password");

        return ResponseEntity.ok(memberService.checkPassword(isLoginedMember, rawPwd));
    }

    @PatchMapping("/edit/other")
    public ResponseEntity<Map<String, Object>> doEditInformation(@RequestParam String email, @RequestParam String phoneNumber, @RequestParam String address) {
        Member isLoginedMember = memberService.getCurrentMember();
        Map<String, Object> response = new HashMap<>();

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
            response.put("success", true);
            response.put("message", "회원 정보 수정에 성공하였습니다.");
            response.put("data", isLoginedMember);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.put("success", false);
        response.put("message", "회원 정보 수정에 실패하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/edit/password")
    public ResponseEntity<Map<String, Object>> editPwd(@RequestParam String MemberPwdConfirm, @RequestParam String memberPwd, @RequestParam String memberPwd2) {
        Member isLoginedMember = memberService.getCurrentMember();
        Map<String, Object> response = new HashMap<>();

        if (memberService.checkPassword(isLoginedMember, MemberPwdConfirm)) {
            if (memberPwd.equals(memberPwd2)) {
                memberService.editPwd(isLoginedMember, memberPwd);
                response.put("success", true);
                response.put("message", "비밀번호 수정에 성공하였습니다.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "입력한 새 비밀번호가 일치하지 않습니다.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            response.put("success", false);
            response.put("message", "현재 비밀번호가 일치하지 않습니다.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


    // admin -----------------------------------------------------------------------------------------------------------
    @GetMapping("/getList")
    public ResponseEntity<Map<String, Object>> getMemberList() {
        Map<String, Object> response = new HashMap<>();
        if (!memberService.getCurrentMember().isAdmin()) {
            response.put("message", "접근 권한이 없습니다.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
        List<Member> members = memberService.getMemberList();
        response.put("members", members);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getWaitingMemberList")
    public ResponseEntity<Map<String, Object>> getWaitingMemberList() {
        Map<String, Object> response = new HashMap<>();
        if(!memberService.getCurrentMember().isAdmin()) {
            List<Member> waitingMembers = memberService.getWaitingLawyerList();
            response.put("waitingMembers", waitingMembers);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "접근 권한이 없습니다.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/approveMember/{id}")
    public ResponseEntity<Map<String, Object>> approveMember(@PathVariable long id) {
        Map<String, Object> response = new HashMap<>();
        String adminLoginId = memberService.getCurrentMember().getMemberId();
        memberService.approveMember(id, adminLoginId);
        response.put("message", "회원 승인에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/editPosition/{id}")
    public ResponseEntity<Map<String, Object>> editPosition(@PathVariable long id, @RequestParam String department, @RequestParam String position) {
        Map<String, Object> response = new HashMap<>();
        if (!memberService.getCurrentMember().isAdmin()) {
            response.put("message", "접근 권한이 없습니다.");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        Member member = memberService.findById(id);
        memberService.editPosition(member, department, position);
        response.put("message", "직급 변경에 성공하였습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}