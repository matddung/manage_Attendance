package com.example.Attendance.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByApproveFirstFalseAndDepartment(String department);
    List<Report> findByApproveFirstTrueAndApproveSecondFalseAndDepartment(String department);
    List<Report> findByApproveSecondTrueAndDepartment(String department);
}