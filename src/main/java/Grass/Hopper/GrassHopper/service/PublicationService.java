package Grass.Hopper.GrassHopper.service;

import Grass.Hopper.GrassHopper.dto.LikeCommentSavedDto;
import Grass.Hopper.GrassHopper.dto.PublicationDto;
import Grass.Hopper.GrassHopper.dto.PublicationResponseDto;
import Grass.Hopper.GrassHopper.dto.UserDto;
import Grass.Hopper.GrassHopper.entity.*;
import Grass.Hopper.GrassHopper.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationService {

    private final LikeRepository likeRepository;
    private final PublicationRepository tweetRepository;
    private final AuthenticationService authenticationService;
    private final RetweetRepository retweetRepository;
    private final UserRepository userRepository;
    private final SavedRepository bookmarkRepository;

    @Transactional
    public void tweet(PublicationDto tweetDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationType type = PublicationType.valueOf(tweetDto.getType());

        if(tweetDto.getTweetId() != null){
            PublicationEntity tweet = tweetRepository.findById(tweetDto.getTweetId()).orElseThrow();

            if(type == PublicationType.REPLY){
                tweet.setReplyCounter(tweet.getReplyCounter() + 1);
                this.createTweet(user, tweetDto.getText(), tweet, type);
            }

            else if(type == PublicationType.QUOTE){
                tweet.setRetweetCounter(tweet.getRetweetCounter() + 1);
                this.createTweet(user, tweetDto.getText(), tweet, type);
            }
            this.tweetRepository.save(tweet);
        }

        else{
            this.createTweet(user, tweetDto.getText(), null, type);
        }

    }

    private void createTweet(UserEntity user, String text, PublicationEntity tweet, PublicationType type){
        this.tweetRepository.save(
                PublicationEntity.builder()
                        .user(user)
                        .text(text)
                        .replyCounter(0)
                        .retweetCounter(0)
                        .likeCounter(0)
                        .tweet(tweet)
                        .type(type)
                        .createdDate(Instant.now())
                        .build()
        );
    }

    @Transactional
    public void deleteTweet(Long id){
        PublicationEntity tweet = this.tweetRepository.findById(id).orElseThrow();
        if(tweet.getType() != PublicationType.TWEET)
        {
            PublicationEntity parentTweet = this.tweetRepository.findById(tweet.getTweet().getId()).orElseThrow();
            if(tweet.getType() == PublicationType.REPLY){
                parentTweet.setReplyCounter(parentTweet.getReplyCounter() - 1);
            }
            else{
                parentTweet.setRetweetCounter(parentTweet.getRetweetCounter() - 1);
            }
            this.tweetRepository.save(parentTweet);
        }
        this.tweetRepository.deleteById(id);
    }

    @Transactional
    public void like(LikeCommentSavedDto likeRetweetBookmarkDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationEntity tweet = this.tweetRepository.findById(likeRetweetBookmarkDto.getTweetId()).orElseThrow();
        Optional<LikeEntity> optional = this.likeRepository.findByUserAndTweet(user, tweet);

        if(optional.isPresent()){
            tweet.setLikeCounter(tweet.getLikeCounter() - 1);
            this.tweetRepository.save(tweet);
            this.likeRepository.delete(optional.get());
        }
        else{
            tweet.setLikeCounter(tweet.getLikeCounter() + 1);
            this.tweetRepository.save(tweet);
            this.likeRepository.save(
                    LikeEntity.builder()
                            .user(user)
                            .tweet(tweet)
                            .build()
            );
        }
    }

    @Transactional
    public void retweet(LikeCommentSavedDto likeRetweetBookmarkDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationEntity tweet = this.tweetRepository.findById(likeRetweetBookmarkDto.getTweetId()).orElseThrow();
        Optional<CommentEntity> retweet = this.retweetRepository.findByUserAndTweet(user, tweet);
        if(retweet.isPresent()){
            tweet.setRetweetCounter(tweet.getRetweetCounter() - 1);
            this.retweetRepository.delete(retweet.get());
        }
        else{
            tweet.setRetweetCounter(tweet.getRetweetCounter() + 1);
            this.retweetRepository.save(
                    CommentEntity.builder()
                            .tweet(tweet)
                            .user(user)
                            .build()
            );
        }
        this.tweetRepository.save(tweet);
    }

    @Transactional(readOnly = true)
    public List<PublicationResponseDto> getAllTweets() {
        List<PublicationEntity> tweets = this.tweetRepository.findAllByType(PublicationType.TWEET).orElseThrow();
        return tweets.stream().map(this::mapTweetToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PublicationResponseDto> getRepliesForTweet(Long id) {
        PublicationEntity tweet = this.tweetRepository.findById(id).orElseThrow();
        List<PublicationEntity> replies = this.tweetRepository.findAllByTweetAndType(tweet, PublicationType.REPLY).orElseThrow();
        return replies.stream().map(this::mapTweetToDto).collect(Collectors.toList());
    }

    @Transactional
    public PublicationResponseDto getTweet(Long id) {
        return this.mapTweetToDto(this.tweetRepository.findById(id).orElseThrow());
    }

    @Transactional(readOnly = true)
    public Boolean isLiked(LikeCommentSavedDto likeRetweetBookmarkDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationEntity tweet = this.tweetRepository.findById(likeRetweetBookmarkDto.getTweetId()).orElseThrow();
        return this.likeRepository.findByUserAndTweet(user, tweet).isPresent();
    }

    @Transactional
    public List<PublicationResponseDto> getAll() {
        List<PublicationResponseDto> tweets = this.tweetRepository.findAll().stream()
                .filter(tweet-> tweet.getType() != PublicationType.REPLY)
                .map(this::mapTweetToDto)
                .collect(Collectors.toList());
        tweets.addAll(this.retweetRepository.findAll().stream().map(this::mapRetweetToDto).collect(Collectors.toList()));
        return tweets;
    }

    @Transactional
    public List<PublicationResponseDto> getTweetsByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow();
        return this.tweetRepository.findAllByUserAndType(user, PublicationType.TWEET).orElseThrow().stream().map(this::mapTweetToDto).collect(Collectors.toList());

    }

    @Transactional
    public List<PublicationResponseDto> getRetweetsByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow();
        List<PublicationResponseDto> tweetResponseDtos = this.tweetRepository.findAllByUserAndType(user, PublicationType.QUOTE)
                .orElseThrow().stream().map(this::mapTweetToDto).collect(Collectors.toList());
        tweetResponseDtos.addAll(this.retweetRepository.findAllByUser(user).orElseThrow().stream().map(this::mapRetweetToDto).collect(Collectors.toList()));

        return tweetResponseDtos;

    }

    @Transactional
    public List<PublicationResponseDto> getRepliesByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow();
        return this.tweetRepository.findAllByUserAndType(user, PublicationType.REPLY).orElseThrow().stream().map(this::mapTweetToDto).collect(Collectors.toList());
    }

    @Transactional
    public List<PublicationResponseDto> getLikedByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username).orElseThrow();
        List<LikeEntity> likes = this.likeRepository.findAllByUser(user).orElseThrow();

        return likes.stream().map(LikeEntity::getTweet).map(this::mapTweetToDto).collect(Collectors.toList());
    }

    private PublicationResponseDto mapTweetToDto(PublicationEntity entity){

        UserEntity user = entity.getUser();
        PublicationResponseDto tweetResponseDto = PublicationResponseDto.builder()
                .id(entity.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .duration(null)
                .tweetText(entity.getText())
                .replyCounter(entity.getReplyCounter())
                .retweetCounter(entity.getRetweetCounter())
                .likeCounter(entity.getLikeCounter())
                .build();

        if(entity.getType() == PublicationType.QUOTE && entity.getTweet() != null){
            tweetResponseDto.setQuote(mapTweetToDto(entity.getTweet()));
        }

        return tweetResponseDto;
    }

    private PublicationResponseDto mapRetweetToDto(CommentEntity retweetEntity) {
        PublicationEntity tweet = retweetEntity.getTweet();
        UserEntity publisher = tweet.getUser();

        UserDto retweeter = UserDto.builder()
                .firstName(retweetEntity.getUser().getFirstName())
                .lastName(retweetEntity.getUser().getLastName())
                .username(retweetEntity.getUser().getUsername())
                .build();

        PublicationResponseDto tweetResponseDto = PublicationResponseDto.builder()
                .id(tweet.getId())
                .firstName(publisher.getFirstName())
                .lastName(publisher.getLastName())
                .username(publisher.getUsername())
                .duration(null)
                .tweetText(tweet.getText())
                .replyCounter(tweet.getReplyCounter())
                .retweetCounter(tweet.getRetweetCounter())
                .likeCounter(tweet.getLikeCounter())
                .retweetedBy(retweeter)
                .build();

        if(retweetEntity.getTweet().getType() == PublicationType.QUOTE){
            tweetResponseDto.setQuote(mapTweetToDto(retweetEntity.getTweet().getTweet()));
        }

        return tweetResponseDto;
    }

    @Transactional
    public void bookmark(LikeCommentSavedDto likeRetweetBookmarkDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationEntity tweet = this.tweetRepository.findById(likeRetweetBookmarkDto.getTweetId()).orElseThrow();
        Optional<SavedEntity> optional = this.bookmarkRepository.findByUserAndTweet(user, tweet);

        if(optional.isPresent()){
            this.bookmarkRepository.delete(optional.get());
        }
        else{
            this.bookmarkRepository.save(SavedEntity.builder().tweet(tweet).user(user).build());
        }
    }

    @Transactional
    public List<PublicationResponseDto> getBookmarksByUsername(String username) {
        UserEntity user = this.userRepository.findByUsername(username)
                .orElseThrow();

        List<SavedEntity> bookmarks = (List<SavedEntity>) this.bookmarkRepository
                .findAllByUser(user)
                .orElseThrow();

        return bookmarks.stream()
                .map(SavedEntity::getTweet)
                .map(this::mapTweetToDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Boolean isBookmarked(LikeCommentSavedDto likeRetweetBookmarkDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationEntity tweet = this.tweetRepository.findById(likeRetweetBookmarkDto.getTweetId()).orElseThrow();
        return this.bookmarkRepository.findByUserAndTweet(user, tweet).isPresent();
    }

    public Integer likeCounter(Long id) {
        return this.tweetRepository.findById(id).orElseThrow().getLikeCounter();
    }

    @Transactional(readOnly = true)
    public Boolean isRetweeted(LikeCommentSavedDto likeRetweetBookmarkDto) {
        UserEntity user = this.authenticationService.getUserFromJwt();
        PublicationEntity tweet = this.tweetRepository.findById(likeRetweetBookmarkDto.getTweetId()).orElseThrow();
        return this.retweetRepository.findByUserAndTweet(user, tweet).isPresent();
    }
}
