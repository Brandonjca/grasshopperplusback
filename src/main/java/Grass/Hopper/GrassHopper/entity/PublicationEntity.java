package Grass.Hopper.GrassHopper.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    private String text;
    private Integer replyCounter;
    private Integer retweetCounter;
    private Integer likeCounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id", referencedColumnName = "id")
    private PublicationEntity tweet;

    @OneToMany(mappedBy = "tweet",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<PublicationEntity> replies;

    private PublicationType type;
    private Instant createdDate;
}
