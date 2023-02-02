package com.footstep.domain.posting.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.repository.CommentRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.footstep.domain.base.BaseResponseStatus.*;


@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public void addComment(String content, Long postingId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));

        Comment comment = new Comment(content, currentUsers, posting);
        commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BaseException(NOT_FOUND_COMMENT));
        comment.changeStatus();
        commentRepository.save(comment);
    }

    public String count(Long postingId) throws BaseException{
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        Integer commentCount = commentRepository.countByPosting(postingId);
        String result = String.valueOf(commentCount);
        return result;
    }

    public void isValid(String field) throws BaseException{
        switch (field) {
            case "content" -> throw new BaseException(COMMENT_EMPTY_CONTENT);
        }
    }
}
