package seifert.back.model.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import seifert.back.model.Facility;

public interface FacilityRepository extends JpaRepository<Facility, Integer> {
}
