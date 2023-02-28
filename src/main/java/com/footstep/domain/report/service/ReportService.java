package com.footstep.domain.report.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.repository.CommentRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.report.domain.Report;
import com.footstep.domain.report.domain.ReportReason;
import com.footstep.domain.report.domain.ReportTarget;
import com.footstep.domain.report.dto.CreateReportDto;
import com.footstep.domain.report.repository.ReportRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.domain.users.service.UsersService;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.footstep.domain.base.BaseResponseStatus.*;

@Transactional
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UsersRepository usersRepository;
    private final PostingRepository postingRepository;
    private final CommentRepository commentRepository;
    private final UsersService usersService;

    public void createReport(CreateReportDto createReportDto, Long targetId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Report> reports = currentUsers.getReports();
        if (reports.contains(targetId)) {
            throw new BaseException(ALREADY_REPORTED);
        }
        switch (createReportDto.getTargetNumber()) {
            case 0 -> {
                Users reportedUser = usersRepository.findById(targetId).orElseThrow(
                        () -> new BaseException(NOT_FOUND_USERS_ID));
                List<Posting> postings = reportedUser.getPostings();
                List<Comment> comments = reportedUser.getComments();
                for (Posting posting : postings) {
                    reportRepository.save(new Report(ReportTarget.USER, ReportReason.get(createReportDto.getReasonNumber()), posting.getId(), currentUsers));
                }
                for (Comment comment : comments) {
                    reportRepository.save(new Report(ReportTarget.USER, ReportReason.get(createReportDto.getReasonNumber()), comment.getId(), currentUsers));
                }
                blockContent(reportedUser);
            }
            case 1 -> {
                Posting posting = postingRepository.findById(targetId).orElseThrow(
                        () -> new BaseException(NOT_FOUND_POSTING));
                reportRepository.save(new Report(ReportTarget.POSTING, ReportReason.get(createReportDto.getReasonNumber()), posting.getId(), currentUsers));
                blockContent(posting.getUsers());
            }
            case 2 -> {
                Comment comment = commentRepository.findById(targetId).orElseThrow(
                        () -> new BaseException(NOT_FOUND_COMMENT));
                reportRepository.save(new Report(ReportTarget.POSTING, ReportReason.get(createReportDto.getReasonNumber()), comment.getId(), currentUsers));
                blockContent(comment.getUsers());
            }
        }
    }

    public void blockContent(Users reportedUser) throws BaseException {
        reportedUser.addReportedCount();
        if (reportedUser.getReportedCount() >= 3) {
            reportedUser.initReportedCount();
            usersService.blocked(reportedUser);
        }
        usersRepository.save(reportedUser);
    }
}
