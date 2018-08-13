package seifert.back.model.repos;

import org.junit.jupiter.api.BeforeEach;
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

    private Facility facilityExpected1;
    private Facility facilityExpected2;

    @BeforeEach
    private void setDatabase() {
        facilityExpected1 = Facility.builder()
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();
        entityManager.persist(facilityExpected1);
        entityManager.flush();

        facilityExpected2 = Facility.builder()
                .name("Second Facility")
                .zipCode(22222)
                .city("Town 2").build();
        entityManager.persist(facilityExpected2);
        entityManager.flush();
    }

    @Test
    public void findAllTest() {

        List<Facility> facilityListActual = facilityRepository.findAll();

        assertEquals(2, facilityListActual.size());
        assertEquals(facilityExpected1, facilityListActual.get(0));
        assertEquals(facilityExpected2, facilityListActual.get(1));

    }

    @Test
    public void findByIDTest() {

        Optional<Facility> facilityActual = facilityRepository.findById(facilityExpected1.getId());

        assertEquals(facilityExpected1, facilityActual.get());
    }

    @Test
    public void saveTest() {

        Facility facilityExpected3 = Facility.builder()
                .name("Facility 3")
                .street("Street Road")
                .houseNumber(9)
                .zipCode(12345)
                .city("Savings City").build();

        Facility facilityActual = facilityRepository.save(facilityExpected3);
        List<Facility> facilityListActual = facilityRepository.findAll();

        assertEquals(facilityExpected3, facilityActual);
        assertEquals(3, facilityListActual.size());
        assertEquals(facilityExpected3, facilityListActual.get(2));

    }

    @Test
    public void deleteByIDTest() {

        facilityRepository.deleteById(facilityExpected1.getId());
        boolean facilityExists = facilityRepository.existsById(facilityExpected1.getId());
        List<Facility> facilityListActual = facilityRepository.findAll();

        assertEquals(false, facilityExists);
        assertEquals(1, facilityListActual.size());
    }

}
