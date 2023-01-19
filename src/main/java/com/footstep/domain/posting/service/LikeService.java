package com.footstep.domain.posting.service;


import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.repository.LikeRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;

    public boolean addLike(Users users, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow();
        if (isNotLiked(users, posting)) {
            likeRepository.save(new Likes(users, posting));
            return true;
        }
        return false;
    }

    public void cancelLike(Users users, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow();
        Likes likes = likeRepository.findByUsersAndPosting(users, posting).orElseThrow();
        likeRepository.delete(likes);
    }

    public String count(Long postingId, Users users) {
        Posting posting = postingRepository.findById(postingId).orElseThrow();
        Integer likeCount = likeRepository.countByPosting(posting).orElse(0);
        String result = String.valueOf(likeCount);
        return result;
    }

    private boolean isNotLiked(Users users, Posting posting) {
        return likeRepository.findByUsersAndPosting(users, posting).isEmpty();
    }
}