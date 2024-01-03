package com.example.Attendance.schedule;

import com.example.Attendance.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Schedule> showList(long id) {
        return scheduleRepository.findByMemberId(id);
    }

    @Transactional
    public Schedule create(LocalDateTime startTime, LocalDateTime endTime, Member member, String subject, String address) {
        Schedule schedule = Schedule.builder()
                .subject(subject)
                .startTime(startTime)
                .endTime(endTime)
                .member(member)
                .address(address)
                .createDate(LocalDateTime.now())
                .build();

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule modify(Long id, String subject, LocalDateTime startTime, LocalDateTime endTime, String address) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        schedule.setSubject(subject);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setAddress(address);

        return scheduleRepository.save(schedule);
    }

    @Transactional
    public Schedule delete(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        scheduleRepository.delete(schedule);

        return schedule;
    }

    public Schedule findById(long id) {
        return scheduleRepository.findById(id).get();
    }
}