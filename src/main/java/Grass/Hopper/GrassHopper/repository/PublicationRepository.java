package Grass.Hopper.GrassHopper.repository;

import Grass.Hopper.GrassHopper.entity.PublicationEntity;
import Grass.Hopper.GrassHopper.entity.PublicationType;
import Grass.Hopper.GrassHopper.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<PublicationEntity, Long> {
    Optional<List<PublicationEntity>> findAllByUserAndType(UserEntity user, PublicationType type);
    Optional<List<PublicationEntity>> findAllByTweetAndType(PublicationEntity tweet, PublicationType type);
    Optional<List<PublicationEntity>> findAllByType(PublicationType type);

}
