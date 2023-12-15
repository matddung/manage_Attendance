package com.example.Attendance.member;

import com.example.Attendance.Util.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> memberSignup(String memberId, String memberPwd, String name, String phoneNumber, String birth) {
        if (findByMemberId(memberId).isPresent()) {
            return RsData.of("F-1", "%s은(는) 이미 사용 중인 아이디입니다.".formatted(memberId));
        }
        if (findByPhoneNumber(phoneNumber).isPresent()) {
            return RsData.of("F-1", "%s은(는) 이미 인증 된 전화번호입니다.".formatted(phoneNumber));
        }

        LocalDateTime now = LocalDateTime.now();

        Member member = Member
                .builder()
                .memberId(memberId)
                .memberPwd(passwordEncoder.encode(memberPwd))
                .name(name)
                .phoneNumber(phoneNumber)
                .birth(birth)
                .build();

        member = memberRepository.save(member);

        return RsData.of("S-1", "회원 가입이 완료되었습니다.", member);
    }

    public Optional<Member> findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    public Optional<Member> findByPhoneNumber(String PhoneNumber) {
        return memberRepository.findByPhoneNumber(PhoneNumber);
    }

    public boolean isAdmin(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(Member::getMemberId)
                .filter(member -> member.equals("administer"))
                .isPresent();
    }

    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }

    public Member getMember(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()) {
            return member.get();
        }
        else {
            throw new RuntimeException("회원 정보가 없습니다.");
        }
    }

    public Member getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return memberRepository.findByMemberId(user.getUsername()).orElse(null);
        }
        return null;
    }
}
