package seifert.back.model.repos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import seifert.back.model.Facility;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class FacilityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FacilityRepository facilityRepository;

    @Test
    public void findAllTest() {

        Facility facilityExpected1 = Facility.builder()
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();
        entityManager.persist(facilityExpected1);
        entityManager.flush();

        Facility facilityExpected2 = Facility.builder()
                .name("Second Facility")
                .street("Road 2")
                .houseNumber(2)
                .zipCode(22222)
                .city("Town 2").build();
        entityManager.persist(facilityExpected2);
        entityManager.flush();

        List<Facility> facilityListActual = facilityRepository.findAll();

        assertEquals(2, facilityListActual.size());
        assertEquals(facilityExpected1, facilityListActual.get(0));
        assertEquals(facilityExpected2, facilityListActual.get(1));

    }

    @Test
    public void findByIDTest() {

        Facility facilityExpected = Facility.builder()
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();
        entityManager.persist(facilityExpected);
        entityManager.flush();

        Optional<Facility> facilityActual = facilityRepository.findById(facilityExpected.getId());

        assertEquals(facilityExpected, facilityActual.get());
    }

}
