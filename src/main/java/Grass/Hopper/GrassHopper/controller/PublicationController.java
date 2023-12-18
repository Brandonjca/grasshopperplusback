package Grass.Hopper.GrassHopper.controller;

import Grass.Hopper.GrassHopper.dto.PublicationDto;
import Grass.Hopper.GrassHopper.dto.PublicationResponseDto;
import Grass.Hopper.GrassHopper.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService tweetService;

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createTweet(@RequestBody PublicationDto tweetDto){
        this.tweetService.tweet(tweetDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteTweet(@PathVariable(name = "id") Long id) {
    this.tweetService.deleteTweet(id);
    return ResponseEntity.ok().build();
}

    @GetMapping("/tweets-by-username/{username}")
    public ResponseEntity<List<PublicationResponseDto>> getTweetsByUsername(@PathVariable(name = "username") String username){
        return ResponseEntity.ok( this.tweetService.getTweetsByUsername(username));
    }

    @GetMapping("/retweets-by-username/{username}")
    public ResponseEntity<List<PublicationResponseDto>> getRetweetsByUsername(@PathVariable(name = "username") String username){
        return ResponseEntity.ok( this.tweetService.getRetweetsByUsername(username));
    }

    @GetMapping("/replies-by-username/{username}")
    public ResponseEntity<List<PublicationResponseDto>> getRepliesByUsername(@PathVariable(name = "username") String username){
        return ResponseEntity.ok( this.tweetService.getRepliesByUsername(username));
    }

    @GetMapping("/liked-by-username/{username}")
    public ResponseEntity<List<PublicationResponseDto>> getLikedByUsername(@PathVariable(name = "username") String username){
        return ResponseEntity.ok( this.tweetService.getLikedByUsername(username));
    }

    @GetMapping
    public ResponseEntity<List<PublicationResponseDto>> getAll(){
        List<PublicationResponseDto> tweets = this.tweetService.getAll();
        return ResponseEntity.ok(tweets);
    }

    @GetMapping("/replies-for-tweet/{id}")
    public ResponseEntity<List<PublicationResponseDto>> getRepliesForTweet(@PathVariable(name = "id") Long id){
        List<PublicationResponseDto> tweets = this.tweetService.getRepliesForTweet(id);
        return ResponseEntity.ok(tweets);
    }
}
