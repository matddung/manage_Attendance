package com.example.Attendance.schedule;

import com.example.Attendance.member.Member;
import com.example.Attendance.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final MemberService memberService;
    Member isLoginedMember = memberService.getCurrentUser();

    @PostMapping("/create")
    public String create(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime, @RequestParam String subject, @RequestParam String address) {
        if (isLoginedMember == null) {
            return "main";
        }

        scheduleService.create(startTime, endTime, isLoginedMember, subject, address);

        return "main";
    }

    @PostMapping("/modify/{id}")
    public String modify(@PathVariable long id, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime, @RequestParam String subject, @RequestParam String address) {
        if (isLoginedMember == null) {
            return "main";
        }

        Schedule schedule = scheduleService.findById(id);

        scheduleService.modify(schedule.getId(), subject, startTime, endTime, address);

        return "main";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        if (isLoginedMember == null) {
            return "main";
        }
        scheduleService.delete(id);
        return "main";
    }
}
