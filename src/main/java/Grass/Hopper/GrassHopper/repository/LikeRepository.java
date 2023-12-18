package Grass.Hopper.GrassHopper.repository;

import Grass.Hopper.GrassHopper.entity.LikeEntity;
import Grass.Hopper.GrassHopper.entity.PublicationEntity;
import Grass.Hopper.GrassHopper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndTweet(UserEntity user, PublicationEntity tweet);
    Optional<List<LikeEntity>> findAllByUser(UserEntity user);
}
