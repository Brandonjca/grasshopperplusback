package Grass.Hopper.GrassHopper.repository;

import Grass.Hopper.GrassHopper.entity.CommentEntity;
import Grass.Hopper.GrassHopper.entity.PublicationEntity;
import Grass.Hopper.GrassHopper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RetweetRepository extends JpaRepository<CommentEntity, Long> {
    Optional<CommentEntity> findByUserAndTweet(UserEntity user, PublicationEntity tweet);
    Optional<CommentEntity> findAllByTweet(PublicationEntity tweet);
    Optional<List<CommentEntity>> findAllByUser(UserEntity user);
}
