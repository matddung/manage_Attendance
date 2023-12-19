package com.example.Attendance.report;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByApproveFirstFalseAndSubmitterDepartment(String department);
    List<Report> findByApproveFirstTrueAndApproveSecondFalseAndSubmitterDepartment(String department);
    List<Report> findByApproveSecondTrueAndSubmitterDepartment(String department);
}