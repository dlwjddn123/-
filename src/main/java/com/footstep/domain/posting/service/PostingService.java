package com.footstep.domain.posting.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.base.Status;
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

    public FeedListResponseDto viewFeed() throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Long> reported = currentUsers.getReports().stream().map(report -> report.getTargetId()).collect(Collectors.toList());
        List<Posting> feeds = postingRepository.findAllFeed(reported, currentUsers);
        if (feeds.isEmpty()){
            throw new BaseException(NOT_FOUND_POSTING);
        }
        List<FeedListDto> feedListDto = new ArrayList<>();

        for (Posting feed : feeds) {
            FeedListDto dto = FeedListDto.builder()
                    .postingId(feed.getId())
                    .usersId(feed.getUsers().getId())
                    .nickname(feed.getUsers().getNickname())
                    .imageUrl(feed.getImageUrl())
                    .title(feed.getTitle())
                    .content(feed.getContent())
                    .likes((long) feed.getLikeList().size())
                    .commentCount(feed.getComments().stream().filter(c -> c.getStatus() == Status.NORMAL).count())
                    .placeName(feed.getPlace().getName())
                    .recordDate(feed.getRecordDate())
                    .build();
            feedListDto.add(dto);
        }
        return new FeedListResponseDto(feedListDto, (long) feedListDto.size());
    }

    public PostingListResponseDto viewSpecificFeedList(Long userId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Users targetUsers = usersRepository.findById(userId)
                .orElseThrow(() -> new BaseException(REQUEST_ERROR));
        List<Long> reported = currentUsers.getReports().stream().map(report -> report.getTargetId()).collect(Collectors.toList());
        List<Posting> feeds = postingRepository.findSpecificFeed(reported, targetUsers);
        if (feeds.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);
        List<PostingListDto> postingListDto = new ArrayList<>();
        List<Date> dates = feeds.stream().map(Posting::getRecordDate).toList();

        for (Posting feed : feeds) {
            PostingListDto dto = PostingListDto.builder()
                    .placeName(feed.getPlace().getName())
                    .recordDate(feed.getRecordDate())
                    .imageUrl(feed.getImageUrl())
                    .title(feed.getTitle())
                    .likes((long) feed.getLikeList().size())
                    .postingCount((long) Collections.frequency(dates, feed.getRecordDate()))
                    .postingId(feed.getId())
                    .build();
            postingListDto.add(dto);
        }
        return new PostingListResponseDto(postingListDto, dates.stream().distinct().count());
    }

    public DesignatedPostingDto viewDesignatedGallery(Date date) throws BaseException {
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
        return new DesignatedPostingDto(postingListDto);
    }

    @Transactional(readOnly = true)
    public SpecificPostingDto viewSpecificPosting(Long postingId) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        Posting posting = postingRepository.findById(postingId)
                .orElseThrow(() -> new BaseException(NOT_FOUND_POSTING));
        Place place = placeRepository.findById(posting.getPlace().getId())
                .orElseThrow(() -> new BaseException(NOT_FOUND_PLACE));
        Integer likeCount = likeRepository.countByPosting(posting).orElse(0);
        List<Long> reported = currentUsers.getReports().stream().map(report -> report.getTargetId()).collect(Collectors.toList());
        List<Comment> comment = commentRepository.findByPosting(posting, reported);
        Integer countComment = commentRepository.countByPosting(postingId);
        //Timestamp postDate = Timestamp.valueOf(posting.getCreatedDate());
        return SpecificPostingDto.builder()
                .postingDate(posting.getRecordDate())
                .postingName(posting.getTitle())
                .content(posting.getContent())
                .imageUrl(posting.getImageUrl())
                .placeName(place.getName())
                .likeNum(Integer.toString(likeCount))
                //.nickName(currentUsers.getNickname())
                .nickName(posting.getUsers().getNickname())
                .commentList(comment.stream()
                        .map(c -> CommentDto.builder().commentId(c.getId()).nickname(c.getUsers().getNickname())
                                .content(c.getContent()).build()).collect(Collectors.toList()))
                .commentNum(Integer.toString(countComment))
                .build();
    }

    public SpecificDateResponseDto viewSpecificDatePosting(Date startDate, Date endDate) throws BaseException {
        Users currentUsers = usersRepository.findByEmail(SecurityUtils.getLoggedUserEmail())
                .orElseThrow(() -> new BaseException(UNAUTHORIZED));
        List<Posting> postings = postingRepository.findByStartDateAndEndDate(currentUsers, startDate, endDate);
        if (postings.isEmpty())
            throw new BaseException(NOT_FOUND_POSTING);

        List<AllPlaceDto> postingListDto = new ArrayList<>();


        for (Posting posting : postings) {
            AllPlaceDto dto = AllPlaceDto.builder()
                    .placeId(posting.getPlace().getId())
                    .placeName(posting.getPlace().getName())
                    .latitude(posting.getPlace().getLatitude())
                    .longitude(posting.getPlace().getLongitude())
                    .build();
            postingListDto.add(dto);
        }
        return new SpecificDateResponseDto(postingListDto.stream().distinct().collect(Collectors.toList()));
    }

    public void isValid(String field) throws BaseException{
        switch (field) {
            case "title" -> throw new BaseException(POSTING_EMPTY_TITLE);
            case "content" -> throw new BaseException(POSTING_EMPTY_CONTENT);
            case "recordDate" -> throw new BaseException(POSTING_INVALID_RECORD_DATE);
            case "createPlaceDto.name" -> throw new BaseException(PLACE_EMPTY_NAME);
            case "createPlaceDto.address" -> throw new BaseException(PLACE_EMPTY_ADDRESS);
            case "createPlaceDto.latitude" -> throw new BaseException(PLACE_INVALID_LATITUDE);
            case "createPlaceDto.longitude" -> throw new BaseException(PLACE_INVALID_LONGITUDE);
            case "visibilityStatusCode" -> throw new BaseException(POSTING_INVALID_STATUS);
        }
    }
}
