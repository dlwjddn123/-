package com.footstep.domain.posting.service;

import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.repository.CommentRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostingRepository postingRepository;

    public boolean addComment(String content, Users users, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow();
        commentRepository.save(new Comment(content, users, posting));
        return true;
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.changeStatus();
        commentRepository.save(comment);
    }

    public String count(Long postingId, Users users) {
        Posting posting = postingRepository.findById(postingId).orElseThrow();
        Integer commentCount = commentRepository.countByPosting(postingId);
        String result = String.valueOf(commentCount);
        return result;
    }
}
