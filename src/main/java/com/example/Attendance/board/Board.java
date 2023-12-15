package com.example.Attendance.board;

import com.example.Attendance.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private Member writer;

    private Long hit;
}