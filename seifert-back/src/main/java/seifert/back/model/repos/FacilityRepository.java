package seifert.back.model.repos;

import org.springframework.data.repository.CrudRepository;
import seifert.back.model.Facility;

public interface FacilityRepository extends CrudRepository<Facility, Long> {
}
