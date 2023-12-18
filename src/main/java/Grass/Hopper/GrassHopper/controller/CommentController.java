package Grass.Hopper.GrassHopper.controller;

import Grass.Hopper.GrassHopper.dto.LikeCommentSavedDto;
import Grass.Hopper.GrassHopper.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/retweets")
@RequiredArgsConstructor
public class CommentController {

    private final PublicationService tweetService;

    @PostMapping("/retweet")
    public ResponseEntity<HttpStatus> postRetweet(@RequestBody LikeCommentSavedDto likeRetweetBookmarkDto){
        this.tweetService.retweet(likeRetweetBookmarkDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/is-retweeted")
    public ResponseEntity<Boolean> isRetweeted(@RequestBody LikeCommentSavedDto likeRetweetBookmarkDto){
        return ResponseEntity.ok(this.tweetService.isRetweeted(likeRetweetBookmarkDto));
    }
}
