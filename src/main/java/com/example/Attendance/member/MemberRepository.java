package com.example.Attendance.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByPhoneNumber(String phoneNumber);
    Member findByDepartmentAndPositionClass(String department, long positionClass);
    Optional<Member> findByNameAndMemberId(String name, String memberId);
}