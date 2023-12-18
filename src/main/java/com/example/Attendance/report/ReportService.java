package com.example.Attendance.report;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public RsData<Report> submitReport(Member submitter, String subject, String content, String category) {
        Report report = Report.builder()
                .submitter(submitter)
                .subject(subject)
                .content(content)
                .category(category)
                .createDate(LocalDateTime.now())
                .isApproveFirst(false)
                .isApproveSecond(false)
                .build();

        report = reportRepository.save(report);

        return new RsData<>("S-1", "보고서가 제출되었습니다.", report);
    }

    @Transactional
    public RsData<Report> firstApproveReport(long id) {
        Report report = reportRepository.findById(id).get();
        Member member = memberRepository.findById(report.getSubmitter().getId()).get();

        Member firstApprovePerson = memberRepository.findByDepartmentAndPositionClass(member.getDepartment(), member.getPositionClass() + 1);

        if (firstApprovePerson == null || firstApprovePerson.getPositionClass() == 2) {
            firstApprovePerson = memberRepository.findByDepartmentAndPositionClass(member.getDepartment(), member.getPositionClass() + 2);
            if (firstApprovePerson == null) {
                firstApprovePerson = memberRepository.findByDepartmentAndPositionClass(member.getDepartment(), member.getPositionClass() + 3);
            }
        }

        report.setFirstApproveDate(LocalDateTime.now());

        report = reportRepository.save(report);

        return new RsData<>("S-1", "1차 승인이 완료되었습니다.", report);
    }

    @Transactional
    public void secondApproveReport() {
    }

    public List<Report> findAllReport() {
        return reportRepository.findByApproveFirstFalse();
    }

    public List<Report> isApproveFirst() {
        return reportRepository.findByApproveFirstTrueAndApproveSecondFalse();
    }

    public List<Report> isApproveAll() {
        return reportRepository.findByApproveSecondTrue();
    }
}