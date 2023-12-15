package com.example.Attendance.Util;

import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@AllArgsConstructor
@Profile("!prod")
public class InitData {
    private final MemberService memberService;

    @Bean
    public ApplicationRunner init() {
        return args -> {
            Optional<Member> member = memberService.findByMemberId("administer");
            if(!member.isPresent()) {
                Member adminMember = memberService.memberSignup("administer", "administer", "관리자", "01012345678", "1996-06-04", "대전 관저동", "administer@administer.com").getData();
            }
        };
    }
}
