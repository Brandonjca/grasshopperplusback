package Grass.Hopper.GrassHopper.controller;

import Grass.Hopper.GrassHopper.dto.LikeCommentSavedDto;
import Grass.Hopper.GrassHopper.dto.PublicationResponseDto;
import Grass.Hopper.GrassHopper.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class SavedController {

    private final PublicationService tweetService;

    @PostMapping("/bookmark")
    public ResponseEntity<HttpStatus> bookmark(@RequestBody LikeCommentSavedDto likeRetweetBookmarkDto){
        this.tweetService.bookmark(likeRetweetBookmarkDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<PublicationResponseDto>> getBookmarks(@PathVariable String username){

        return ResponseEntity.ok(this.tweetService.getBookmarksByUsername(username));
    }

    @PostMapping("/is-bookmarked")
    public ResponseEntity<Boolean> isBookmarked(@RequestBody LikeCommentSavedDto likeRetweetBookmarkDto){
        return ResponseEntity.ok(this.tweetService.isBookmarked(likeRetweetBookmarkDto));
    }
}
