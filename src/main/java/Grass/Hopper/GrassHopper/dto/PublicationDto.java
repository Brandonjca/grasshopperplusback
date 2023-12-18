package Grass.Hopper.GrassHopper.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationDto {
    private String text;
    private Long tweetId;
    private String type;
}
