package com.footstep.domain.posting.service;


import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.BaseResponse;
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

import static com.footstep.domain.base.BaseResponseStatus.*;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;
    private final UsersRepository usersRepository;

    public void addLike(Long postingId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        Likes likes = new Likes(currentUsers, posting);
        likeRepository.save(new Likes(currentUsers, posting));
    }

    public void cancelLike(Long postingId) throws BaseException{
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(()->new BaseException(NOT_FOUND_POSTING));
        Likes likes = likeRepository.findByUsersAndPosting(currentUsers, posting)
                .orElseThrow(() -> new BaseException(NOT_FOUND_LIKE));
        likeRepository.delete(likes);
    }

    public String count(Long postingId) throws BaseException{
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(()->new BaseException(NOT_FOUND_POSTING));
        Integer likeCount = likeRepository.countByPosting(posting).orElse(0);
        String result = String.valueOf(likeCount);
        return result;
    }

}