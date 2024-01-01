package com.example.Attendance.member;

import com.example.Attendance.Util.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RsData<Member> memberSignup(String memberId, String memberPwd, String name, String phoneNumber, String birth, String address, String email) {
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
                .address(address)
                .email(email)
                .current("waiting")
                .build();

        member = memberRepository.save(member);

        return RsData.of("S-1", "회원 가입이 신청이 완료되었습니다.", member);
    }

    public Optional<Member> findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId);
    }

    public Optional<Member> findByPhoneNumber(String PhoneNumber) {
        return memberRepository.findByPhoneNumber(PhoneNumber);
    }

    public List<Member> getMemberList() {
        return memberRepository.findByCurrent("approve");
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

    public Member getCurrentMember() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return memberRepository.findByMemberId(user.getUsername()).orElse(null);
        }
        return null;
    }

    public Member editPwd(Member member, String newMemberPwd) {
        member.setMemberPwd(passwordEncoder.encode(newMemberPwd));
        return memberRepository.save(member);
    }

    public Member editInfo(Member member, String phoneNumber, String email, String address) {
        member.setEmail(email);
        member.setPhoneNumber(phoneNumber);
        member.setAddress(address);

        return memberRepository.save(member);
    }

    public Member editPosition(Member member, String department, String position) {
        member.setDepartment(department);
        member.setPosition(position);

        return memberRepository.save(member);
    }

    public boolean checkPassword (Member member, String rawPwd) {
        return passwordEncoder.matches(rawPwd, member.getMemberPwd());
    }

    public Member findById(long id) {
        return memberRepository.findById(id).get();
    }

    public Member findByNameAndMemberId(String name, String memberId) {
        Optional<Member> member = memberRepository.findByNameAndMemberId(name, memberId);
        if(member.isPresent()) {
            return member.get();
        } else {
            throw new RuntimeException("일치하는 회원 정보가 없습니다.");
        }
    }

    @Transactional
    public void approveMember(long id, String memberId) {
        if (!isAdmin(memberId)) {
            throw new RuntimeException("승인 권한이 없습니다.");
        }

        Optional<Member> optionalMember = memberRepository.findById(id);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if ("waiting".equals(member.getCurrent())) {
                member.setCurrent("approve");
                memberRepository.save(member);
            } else {
                throw new RuntimeException("선택된 유저는 승인 대기 중인 상태가 아닙니다.");
            }
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }

    public boolean isAdmin(String userLoginId) {
        return memberRepository.findByMemberId(userLoginId)
                .map(Member::getMemberId)
                .filter(loginId -> loginId.equals("administer"))
                .isPresent();
    }

    public List<Member> getWaitingLawyerList() {
        List<Member> members = memberRepository.findByCurrent("waiting");
        if (members.isEmpty()) {
            throw new RuntimeException("승인 대기 중인 유저 목록이 없습니다.");
        }
        return members;
    }
}