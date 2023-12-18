package Grass.Hopper.GrassHopper.controller;

import Grass.Hopper.GrassHopper.dto.LikeCommentSavedDto;
import Grass.Hopper.GrassHopper.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final PublicationService tweetService;

    @PostMapping("/like")
    public ResponseEntity<HttpStatus> like(@RequestBody LikeCommentSavedDto likeRetweetBookmarkDto){
        this.tweetService.like(likeRetweetBookmarkDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(@RequestBody LikeCommentSavedDto likeRetweetBookmarkDto){
        return ResponseEntity.ok(this.tweetService.isLiked(likeRetweetBookmarkDto));
    }

    @GetMapping("/like-counter/{id}")
    public ResponseEntity<Integer> likeCounter(@PathVariable(name = "id") Long id){
        return ResponseEntity.ok(this.tweetService.likeCounter(id));
    }

}
