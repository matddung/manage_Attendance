package com.example.Attendance.schedule;

import com.example.Attendance.Util.RsData;
import com.example.Attendance.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public RsData<Schedule> create(LocalDateTime startTime, LocalDateTime endTime, Member member, String subject, String address) {
        Schedule schedule = Schedule.builder()
                .subject(subject)
                .startTime(startTime)
                .endTime(endTime)
                .member(member)
                .address(address)
                .createDate(LocalDateTime.now())
                .build();

        schedule = scheduleRepository.save(schedule);

        return new RsData<>("S-1", "일정이 생성되었습니다.", schedule);
    }

    @Transactional
    public RsData<Schedule> modify(Long id, String subject, LocalDateTime startTime, LocalDateTime endTime, String address) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        schedule.setSubject(subject);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setAddress(address);

        scheduleRepository.save(schedule);

        return new RsData<>("S-1", "일정이 수정되었습니다.", schedule);
    }

    @Transactional
    public RsData<Schedule> delete(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElse(null);

        scheduleRepository.delete(schedule);

        return new RsData<>("S-1", "일정이 삭제되었습니다.", schedule);
    }
}