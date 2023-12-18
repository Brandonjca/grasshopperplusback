package Grass.Hopper.GrassHopper.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicationResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String duration;
    private String tweetText;
    private Integer replyCounter;
    private Integer retweetCounter;
    private Integer likeCounter;
    private UserDto retweetedBy;
    private PublicationResponseDto quote;

}
