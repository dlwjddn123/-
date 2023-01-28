package com.footstep.domain.posting.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.posting.domain.Comment;
import com.footstep.domain.posting.domain.place.Place;
import com.footstep.domain.posting.domain.posting.Posting;
import com.footstep.domain.posting.dto.*;
import com.footstep.domain.posting.repository.CommentRepository;
import com.footstep.domain.posting.repository.LikeRepository;
import com.footstep.domain.posting.repository.PlaceRepository;
import com.footstep.domain.posting.repository.PostingRepository;
import com.footstep.domain.users.domain.Users;
import com.footstep.domain.users.repository.UsersRepository;
import com.footstep.global.config.s3.S3UploadUtil;
import com.footstep.global.config.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

import static com.footstep.domain.base.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostingService {
    
    private final PlaceService placeService;
    private final UsersRepository usersRepository;
    private final PostingRepository postingRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final PlaceRepository placeRepository;
    private final S3UploadUtil s3UploadUtil;
    
    public void uploadPosting(MultipartFile image, CreatePostingDto createPostingDto) throws BaseException, IOException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        CreatePlaceDto createPlaceDto = createPostingDto.getCreatePlaceDto();
        Optional<Place> place = placeService.getPlace(createPlaceDto);
        Place createPlace;
        String imageUrl = null;
        if (!image.isEmpty()) {
            imageUrl = s3UploadUtil.upload(image);
        }
        if (place.isEmpty())
            createPlace = placeService.createPlace(createPlaceDto);
        else
            createPlace = place.get();
        Posting posting = Posting.builder()
                .title(createPostingDto.getTitle())
                .content(createPostingDto.getContent())
                .recordDate(createPostingDto.getRecordDate())
                .imageUrl(imageUrl)
                .place(createPlace)
                .users(currentUsers)
                .visibilityStatusCode(createPostingDto.getVisibilityStatusCode())
                .build();

        postingRepository.save(posting);
    }

    public EditPostingDto getPostingInfo(Long postingId) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        Place place = posting.getPlace();
        CreatePlaceDto createPlaceDto = CreatePlaceDto.builder()
                .address(place.getAddress())
                .name(place.getName())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .build();
        EditPostingDto editPostingDto = EditPostingDto.builder()
                .createPlaceDto(createPlaceDto)
                .title(posting.getTitle())
                .content(posting.getContent())
                .recordDate(posting.getRecordDate())
                .visibilityStatusCode(posting.getVisibilityStatus().getCode())
                .imageUrl(posting.getImageUrl())
                .build();
        return editPostingDto;
    }

    public void editPosting(Long postingId, MultipartFile image, CreatePostingDto createPostingDto) throws BaseException, IOException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
            .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        CreatePlaceDto createPlaceDto = createPostingDto.getCreatePlaceDto();
        Optional<Place> place = placeService.getPlace(createPlaceDto);
        Place createPlace;
        String imageUrl = null;
        if (!image.isEmpty()) {
            imageUrl = s3UploadUtil.upload(image);
        }
        if (place.isEmpty())
            createPlace = placeService.createPlace(createPlaceDto);
        else
            createPlace = place.get();
        posting.editPosting(createPostingDto, createPlace, imageUrl);
        postingRepository.save(posting);
    }

    public void removePosting(Long postingId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        posting.removePosting();
        postingRepository.save(posting);
    }

    public PostingListResponseDto viewGallery() throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Posting> postings = postingRepository.findByUsers(users);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        List<PostingListDto> postingListDto = new ArrayList<>();
        List<Date> dates = postings.stream().map(Posting::getRecordDate).toList();

        for (Posting posting : postings) {
            PostingListDto dto = PostingListDto.builder()
                    .placeName(posting.getPlace().getName())
                    .recordDate(posting.getRecordDate())
                    .imageUrl(posting.getImageUrl())
                    .title(posting.getTitle())
                    .likes((long) posting.getLikeList().size())
                    .postingCount((long) Collections.frequency(dates, posting.getRecordDate()))
                    .postingId(posting.getId())
                    .build();
            postingListDto.add(dto);
        }
        return new PostingListResponseDto(postingListDto, dates.stream().distinct().count());
    }

    public GalleryListResponseDto viewDesignatedGallery(Date date) throws BaseException {
        Users users = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Posting> postings = postingRepository.findByUsersAndRecordDate(users,date);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        List<PostingListDto> postingListDto = new ArrayList<>();
        List<Date> dates = postings.stream().map(Posting::getRecordDate).toList();

        for (Posting posting : postings) {
            PostingListDto dto = PostingListDto.builder()
                    .placeName(posting.getPlace().getName())
                    .recordDate(posting.getRecordDate())
                    .imageUrl(posting.getImageUrl())
                    .title(posting.getTitle())
                    .likes((long) posting.getLikeList().size())
                    .postingCount((long) Collections.frequency(dates, date))
                    .postingId(posting.getId())
                    .build();
            postingListDto.add(dto);
        }
        return new GalleryListResponseDto(postingListDto);
    }

    @Transactional(readOnly = true)
    public SpecificPosting viewSpecificPosting(Long postingId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        Place place = placeRepository.findById(posting.getPlace().getId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLACE));
        Integer likeCount = likeRepository.countByPosting(posting).orElse(0);
        List<Comment> comment = commentRepository.findByPosting(posting);


        Integer countComment = commentRepository.countByPosting(postingId);


        Timestamp postDate = Timestamp.valueOf(posting.getCreatedDate());
        return SpecificPosting.builder()
                .postingDate(postDate)
                .postingName(posting.getTitle())
                .content(posting.getContent())
                .imageUrl(posting.getImageUrl())
                .placeName(place.getName())
                .likeNum(Integer.toString(likeCount))
                .nickName(currentUsers.getNickname())
                .commentList(comment.stream()
                        .map(c -> CommentDto.builder().commentId(c.getId()).nickname(c.getUsers().getNickname())
                                .content(c.getContent()).build()).collect(Collectors.toList()))
                .commentNum(Integer.toString(countComment))
                .build();
    }
}
