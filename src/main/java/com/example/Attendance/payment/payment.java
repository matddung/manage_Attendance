package com.example.Attendance.payment;

import com.example.Attendance.member.Member;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
public class payment {
    private long id;

    private String category;

    private LocalDateTime createDate;

    private LocalDateTime paymentDate;

    private Member paymentPerson;

    private String subject;

    private String content;
}
