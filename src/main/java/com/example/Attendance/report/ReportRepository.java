package com.example.Attendance.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByApproveFirstFalse();
    List<Report> findByApproveFirstTrueAndApproveSecondFalse();
    List<Report> findByApproveSecondTrue();
}