package seifert.back.model.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import seifert.back.model.Facility;

import java.util.Optional;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    Optional<Facility> findFacilityByName(String name);
}
