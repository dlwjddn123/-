package com.footstep.domain.posting.service;


import com.footstep.domain.base.BaseException;
import com.footstep.domain.posting.domain.Likes;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.repository.LikeRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.footstep.domain.base.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public String like(Long postingId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        Optional<Likes> isLike = likeRepository.findByUsersAndPosting(currentUsers, posting);
        if (!isLike.isEmpty()) {
            likeRepository.delete(isLike.get());
            return "좋아요를 취소하였습니다.";
        } else {
            likeRepository.save(new Likes(currentUsers, posting));
            return "좋아요를 눌렀습니다.";
        }
    }

    public String count(Long postingId) throws BaseException{
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(()->new BaseException(NOT_FOUND_POSTING));
        Integer likeCount = likeRepository.countByPosting(posting).orElse(0);
        String result = String.valueOf(likeCount);
        return result;
    }
}