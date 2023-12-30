package com.example.Attendance.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@Component
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@DynamicInsert
@DynamicUpdate
@SuperBuilder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String memberId;

    private String memberPwd;

    private String birth;

    private String address;

    private String email;

    private String department;

    private String position;

    private String current;

    // ex) 1 = 사원, 2 = 대리, 3 = 과장, 4 = 부장, 5 = 대표 이사
    private int positionClass;

    public boolean isAdmin() {
        return "administer".equals(memberId);
    }
}