package com.footstep.domain.report.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.mail.service.MailService;
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

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
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
    private final MailService mailService;

    public void createReport(CreateReportDto createReportDto, Long targetId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Long> reports = currentUsers.getReports().stream().map(Report::getTargetId).toList();
        List<Long> reportedUsersIds = currentUsers.getReports().stream().map(Report::getReportedUsersId).toList();
        if (reports.contains(targetId)) {
            throw new BaseException(ALREADY_REPORTED);
        }
        switch (createReportDto.getTargetNumber()) {
            case 0 -> {
                Users reportedUser = usersRepository.findById(targetId).orElseThrow(
                        () -> new BaseException(NOT_FOUND_USERS_ID));
                List<Posting> postings = reportedUser.getPostings();
                List<Comment> comments = reportedUser.getComments();
                if (!reportedUsersIds.contains(reportedUser.getId())) {
                    reportedUser.addReportedCount();
                }
                reportRepository.save(new Report(ReportTarget.USER, ReportReason.get(createReportDto.getReasonNumber()),
                        reportedUser.getId(), reportedUser.getId(), currentUsers));
                for (Posting posting : postings) {
                    if (!reports.contains(posting.getId())) {
                        reportRepository.save(new Report(ReportTarget.USER, ReportReason.get(createReportDto.getReasonNumber()),
                                posting.getId(), reportedUser.getId(), currentUsers));
                    }
                }
                for (Comment comment : comments) {
                    if (!reports.contains(comment.getId())) {
                        reportRepository.save(new Report(ReportTarget.USER, ReportReason.get(createReportDto.getReasonNumber()),
                                comment.getId(), reportedUser.getId(), currentUsers));
                    }
                }
                blockContent(reportedUser);
            }
            case 1 -> {
                Posting posting = postingRepository.findById(targetId).orElseThrow(
                        () -> new BaseException(NOT_FOUND_POSTING));
                if (!reportedUsersIds.contains(posting.getUsers().getId())) {
                    posting.getUsers().addReportedCount();
                }
                reportRepository.save(new Report(ReportTarget.POSTING, ReportReason.get(createReportDto.getReasonNumber()),
                        posting.getId(), posting.getUsers().getId(), currentUsers));
                blockContent(posting.getUsers());
            }
            case 2 -> {
                Comment comment = commentRepository.findById(targetId).orElseThrow(
                        () -> new BaseException(NOT_FOUND_COMMENT));
                if (!reportedUsersIds.contains(comment.getUsers().getId())) {
                    comment.getUsers().addReportedCount();
                }
                reportRepository.save(new Report(ReportTarget.POSTING, ReportReason.get(createReportDto.getReasonNumber()),
                        comment.getId(), comment.getUsers().getId(), currentUsers));
                blockContent(comment.getUsers());
            }
        }
    }

    public void blockContent(Users reportedUser) throws BaseException {
        if (reportedUser.getReportedCount() >= 3) {
            reportedUser.initReportedCount();
            try{
                mailService.sendMail(reportedUser.getEmail());
            } catch (UnsupportedEncodingException exception) {
                throw new BaseException(INVALID_CHAR_SET);
            } catch (MessagingException exception) {
                throw new BaseException(INVALID_CHAR_SET);
            }
            usersService.blocked(reportedUser);
        }
        usersRepository.save(reportedUser);
    }
}
