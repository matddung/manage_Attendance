package com.example.Attendance.report;

import com.example.Attendance.Util.Rq;
import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping("/list")
    public String showReportList(Model model) {
        Member member = memberService.getCurrentUser();
        List<Report> reports = reportService.findAllReport(member.getDepartment());
        model.addAttribute("reports", reports);
        return "report_list";
    }

    @GetMapping("/submit")
    public String showSubmitReport() {
        Member isLoginedMember = memberService.getCurrentUser();

        if(isLoginedMember == null) {
            return "main";
        }

        return "report_submit";
    }

    @PostMapping("/submit")
    public String doSubmitReport(@RequestParam String subject, @RequestParam String content, @RequestParam String category, Model model) {
        Member isLoginedMember = memberService.getCurrentUser();

        RsData<Report> report = reportService.submitReport(isLoginedMember, subject, content, category);
        RsData<Report> assign = reportService.assignApprovePerson(report.getData().getId());

        if (!assign.getData().equals("F-1")) {
            model.addAttribute("error", "승인자 할당에 실패하였습니다.");
            reportService.delete(report.getData());
            return "main";
        }

        return "redirect:/report";
    }

    @GetMapping("/detail/{id}")
    public String reportDetail(@PathVariable long id, Model model) {
        Member isLoginedMember = memberService.getCurrentUser();

        if(isLoginedMember == null) {
            return "main";
        }

        Report report = reportService.findById(id).get();

        model.addAttribute("report", report);

        return "report_detail";
    }

    @PostMapping("/firstApprove/{id}")
    public String firstApproveReport(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentUser();
        Report report = reportService.findById(id).get();

        if(isLoginedMember.getPositionClass() == report.getFirstApprovePerson().getPositionClass()) {
            reportService.firstApproveReport(report);
        } else {
            return rq.redirect("/report", "권한이 없습니다.");
        }

        return rq.redirect("/report", "1차 승인이 완료되었습니다.");
    }

    @PostMapping("/secondApprove/{id}")
    public String secondApproveReport(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentUser();
        Report report = reportService.findById(id).get();

        if(isLoginedMember.getPositionClass() == report.getSecondApprovePerson().getPositionClass()) {
            reportService.secondApproveReport(report);
        } else {
            return rq.redirect("/report", "권한이 없습니다.");
        }

        return rq.redirect("/report", "최종 승인이 완료되었습니다.");
    }

    @PostMapping("/reject/{id}")
    public String rejectReport(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentUser();
        Report report = reportService.findById(id).get();

        if(isLoginedMember.getPositionClass() == report.getSecondApprovePerson().getPositionClass() || isLoginedMember.getPositionClass() == report.getFirstApprovePerson().getPositionClass()) {
            reportService.rejectReport(report);
        }

        return "redirect:/report/list";
    }
}