package com.example.Attendance.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/create")
    public String create() {
        return "main";
    }

    @PostMapping("/modify/{id}")
    public String modify() {
        return "main";
    }

    @PostMapping("/delete/{id}")
    public String delete() {
        return "main";
    }
}
