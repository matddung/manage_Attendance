package com.example.Attendance.schedule;

import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final MemberService memberService;

    @GetMapping("/list")
    public List<Schedule> list() {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        List<Schedule> schedules = scheduleService.showList(isLoginedMember.getId());
        return schedules;
    }

    @PostMapping("/create")
    public Schedule create(@Parameter(name = "startTime") @RequestParam LocalDateTime startTime,
                           @Parameter(name = "endTime") @RequestParam LocalDateTime endTime,
                           @Parameter(name = "subject") @RequestParam String subject,
                           @Parameter(name = "address") @RequestParam String address) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        return scheduleService.create(startTime, endTime, isLoginedMember, subject, address);
    }

    @PatchMapping("/modify/{id}")
    public Schedule modify(@PathVariable long id,
                           @Parameter(name = "startTime") @RequestParam LocalDateTime startTime,
                           @Parameter(name = "endTime") @RequestParam LocalDateTime endTime,
                           @Parameter(name = "subject") @RequestParam String subject,
                           @Parameter(name = "address") @RequestParam String address) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        Schedule schedule = scheduleService.findById(id);
        if (isLoginedMember == scheduleService.findById(id).getMember()) {
            return scheduleService.modify(schedule.getId(), subject, startTime, endTime, address);
        } else {
            throw new RuntimeException("수정 권한이 없습니다.");
        }
    }

    @PostMapping("/delete/{id}")
    public Schedule delete(@PathVariable long id) {
        Member isLoginedMember = memberService.getCurrentMember();
        if (isLoginedMember == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }
        if (isLoginedMember == scheduleService.findById(id).getMember()) {
            return scheduleService.delete(id);
        } else {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }
}
