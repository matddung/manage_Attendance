package com.example.Attendance.report;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
                .current("승인 대기")
                .build();

        report = reportRepository.save(report);

        return new RsData<>("S-1", "보고서가 제출되었습니다.", report);
    }

    @Transactional
    public RsData<Report> assignApprovePerson(long id) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report not found for this id : " + id));
        long submitterId = report.getSubmitter().getId();
        Member member = memberRepository.findById(submitterId).orElseThrow(() -> new IllegalArgumentException("Member not found for this id : " + submitterId));

        Member firstApprovePerson = memberRepository.findByDepartmentAndPositionClass(member.getDepartment(), member.getPositionClass() + 1);

        if (firstApprovePerson == null || firstApprovePerson.getPositionClass() == 2) {
            firstApprovePerson = memberRepository.findByDepartmentAndPositionClass(member.getDepartment(), member.getPositionClass() + 2);
            if (firstApprovePerson == null) {
                firstApprovePerson = memberRepository.findByDepartmentAndPositionClass(member.getDepartment(), member.getPositionClass() + 3);
                if (firstApprovePerson == null) {
                    throw new IllegalArgumentException("First Approve Person not found for this department : " + member.getDepartment() + " and positionClass : " + (member.getPositionClass() + 3));
                }
            }
        }

        Member secondApprovePerson = memberRepository.findByDepartmentAndPositionClass(firstApprovePerson.getDepartment(), firstApprovePerson.getPositionClass() + 1);

        if (secondApprovePerson == null || secondApprovePerson.getPositionClass() == 7) {
            throw new IllegalArgumentException("Second Approve Person not found for this department : " + firstApprovePerson.getDepartment() + " and positionClass : " + (firstApprovePerson.getPositionClass() + 1));
        }

        report = reportRepository.save(report);

        return new RsData<>("S-1", "담당자가 배정되었습니다.", report);
    }

    public Report firstApproveReport(Report report) {
        report.setFirstApproveDate(LocalDateTime.now());
        report.setApproveFirst(true);
        return reportRepository.save(report);
    }

    public Report secondApproveReport(Report report) {
        report.setSecondApproveDate(LocalDateTime.now());
        report.setApproveSecond(true);
        report.setCurrent("승인");
        return reportRepository.save(report);
    }

    public void rejectReport(Report report) {
        report.setCurrent("거부");
    }

    public List<Report> findAllReport(String department) {
        return reportRepository.findByApproveFirstFalseAndDepartment(department);
    }

    public List<Report> isApproveFirst(String department) {
        return reportRepository.findByApproveFirstTrueAndApproveSecondFalseAndDepartment(department);
    }

    public List<Report> isApproveAll(String department) {
        return reportRepository.findByApproveSecondTrueAndDepartment(department);
    }

    public void delete(Report report) {
        reportRepository.delete(report);
    }

    public Optional<Report> findById(long id) {
        return reportRepository.findById(id);
    }
}