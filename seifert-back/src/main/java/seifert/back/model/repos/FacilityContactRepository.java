package seifert.back.model.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import seifert.back.model.Facility;
import seifert.back.model.FacilityContact;

import java.util.List;
import java.util.Optional;

public interface FacilityContactRepository extends JpaRepository<FacilityContact, Integer> {
    List<FacilityContact> findFacilityContactsByFacility(Facility facility);
    Optional<FacilityContact> findFacilityContactByNameAndFacility(String name, Facility facility);
}
