package com.footstep.domain.report.repository;

import com.footstep.domain.report.domain.Report;
import com.footstep.domain.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUsers(Users users);
}
