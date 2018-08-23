package seifert.back.model.repos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import seifert.back.model.Facility;
import seifert.back.model.FacilityContact;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class FacilityContactRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FacilityContactRepository facilityContactRepository;

    private FacilityContact facilityContactExpected1;
    private FacilityContact facilityContactExpected2;
    private Facility facilityExpected1;

    @BeforeEach
    private void setDatabase() {
        facilityExpected1 = Facility.builder()
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();
        entityManager.persist(facilityExpected1);

        facilityContactExpected1 = FacilityContact.builder()
                .name("Important Person")
                .emailAddress("person@important.com")
                .telephoneNumber("00123456789")
                .facility(facilityExpected1)
                .build();
        entityManager.persist(facilityContactExpected1);

        facilityContactExpected2 = FacilityContact.builder()
                .name("Berta Boring")
                .emailAddress("bored@soooboring.org")
                .facility(facilityExpected1)
                .build();
        entityManager.persist(facilityContactExpected2);
        entityManager.flush();
    }

    @Test
    public void findFacilityContactsByFacilityTest() {
        List<FacilityContact> contactListActual =
                facilityContactRepository.findFacilityContactsByFacility(facilityExpected1);
        assertEquals(2, contactListActual.size());
        assertEquals(facilityContactExpected1, contactListActual.get(0));
        assertEquals(facilityContactExpected2, contactListActual.get(1));
    }

    @Test
    public void findFacilityContactByNameAndFacilityTest() {
        Optional<FacilityContact> facilityContactActual = facilityContactRepository
                .findFacilityContactByNameAndFacility(facilityContactExpected1.getName(), facilityExpected1);
        Optional<FacilityContact> facilityContactActualEmpty = facilityContactRepository
                .findFacilityContactByNameAndFacility("Karl", facilityExpected1);
        assertTrue(facilityContactActual.isPresent());
        assertEquals(facilityContactExpected1, facilityContactActual.get());
        assertFalse(facilityContactActualEmpty.isPresent());
    }

    @Test
    public void findByIDTest() {
        Optional<FacilityContact> contactActual = facilityContactRepository.findById(facilityContactExpected1.getId());
        Optional<FacilityContact> contactActualEmpty = facilityContactRepository.findById(5);
        assertTrue(contactActual.isPresent());
        assertEquals(facilityContactExpected1, contactActual.get());
        assertFalse(contactActualEmpty.isPresent());
    }

    @Test
    public void saveTest() {

        FacilityContact facilityContactExpected3 = FacilityContact.builder()
                .name("Leonard Lang").facility(facilityExpected1).build();

        FacilityContact facilityContactActual = facilityContactRepository.save(facilityContactExpected3);
        boolean facilityContactExists = facilityContactRepository.existsById(facilityContactExpected3.getId());
        List<FacilityContact> facilityContactListActual =
                facilityContactRepository.findFacilityContactsByFacility(facilityExpected1);

        assertEquals(facilityContactExpected3, facilityContactActual);
        assertTrue(facilityContactExists);
        assertEquals(3, facilityContactListActual.size());
        assertEquals(facilityContactExpected3, facilityContactListActual.get(2));
    }

    @Test
    public void deleteByIDTest() {
        facilityContactRepository.deleteById(facilityContactExpected1.getId());

        boolean facilityContactExists = facilityContactRepository.existsById(facilityContactExpected1.getId());
        List<FacilityContact> contactListActual =
                facilityContactRepository.findFacilityContactsByFacility(facilityExpected1);

        assertFalse(facilityContactExists);
        assertEquals(1, contactListActual.size());
    }

    @Test
    public void existsByIDTest() {
        assertTrue(facilityContactRepository.existsById(facilityContactExpected1.getId()));
        assertFalse(facilityContactRepository.existsById(99));
    }
}
