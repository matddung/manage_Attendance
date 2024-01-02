package com.example.Attendance.report;

import com.example.Attendance.member.Member;
import jakarta.persistence.*;
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
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String category;

    private LocalDateTime createDate;

    @ManyToOne
    private Member submitter;

    private String subject;

    private String content;

    private LocalDateTime firstApproveDate;

    @ManyToOne
    private Member firstApprovePerson;

    private boolean approveFirst;

    private LocalDateTime secondApproveDate;

    @ManyToOne
    private Member secondApprovePerson;

    private boolean approveSecond;

    private String current;

    private String type;
}