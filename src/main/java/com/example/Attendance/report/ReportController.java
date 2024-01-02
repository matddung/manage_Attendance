package com.example.Attendance.report;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/list")
    public List<Report> showReportList() {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        List<Report> reports = reportService.findAllReport(isLoginedMember.getDepartment());
        return reports;
    }

    @GetMapping("/approveList")
    public List<Report> showSubmitReport() {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        List<Report> submitReports = reportService.isApproveAll(isLoginedMember.getDepartment());
        return submitReports;
    }

    @PostMapping("/submit")
    @Transactional
    public Report doSubmitReport(@Parameter(name = "subject") @RequestParam String subject,
                                 @Parameter(name = "content") @RequestParam String content,
                                 @Parameter(name = "category", example = "연차, 프로젝트, 제안서") @RequestParam String category) {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        RsData<Report> report = reportService.submitReport(isLoginedMember, subject, content, category);
        RsData<Report> assign = reportService.assignApprovePerson(report.getData().getId());
        if (!assign.getData().equals("F-1")) {
            throw new RuntimeException("승인자 할당에 실패하였습니다.");
        }
        return assign.getData();
    }

    @GetMapping("/detail/{id}")
    public Report showReportDetail(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Report report = reportService.findById(id).get();
        return report;
    }

    @PostMapping("/firstApprove/{id}")
    public Report firstApproveReport(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Report report = reportService.findById(id).get();
        if(isLoginedMember.getPositionClass() == report.getFirstApprovePerson().getPositionClass()) {
            return reportService.firstApproveReport(report);
        } else {
            throw new RuntimeException("승인 권한이 없습니다.");
        }
    }

    @PostMapping("/secondApprove/{id}")
    public Report secondApproveReport(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Report report = reportService.findById(id).get();
        if(isLoginedMember.getPositionClass() == report.getSecondApprovePerson().getPositionClass()) {
            return reportService.secondApproveReport(report);
        } else {
            throw new RuntimeException("승인 권한이 없습니다.");
        }
    }

    @PostMapping("/reject/{id}")
    public Report rejectReport(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if(isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Report report = reportService.findById(id).get();
        if(isLoginedMember.getPositionClass() == report.getSecondApprovePerson().getPositionClass() || isLoginedMember.getPositionClass() == report.getFirstApprovePerson().getPositionClass()) {
            return reportService.rejectReport(report);
        } else {
            throw new RuntimeException("거부 권한이 없습니다.");
        }
    }
}