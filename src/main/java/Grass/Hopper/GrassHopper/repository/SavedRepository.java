package Grass.Hopper.GrassHopper.repository;

import antlr.collections.List;
import Grass.Hopper.GrassHopper.entity.SavedEntity;
import Grass.Hopper.GrassHopper.entity.PublicationEntity;
import Grass.Hopper.GrassHopper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface SavedRepository extends JpaRepository<SavedEntity, Long> {

    Optional<SavedEntity> findByUserAndTweet(UserEntity user, PublicationEntity tweet);
    java.util.Optional<List> findAllByUser(UserEntity user);

}
