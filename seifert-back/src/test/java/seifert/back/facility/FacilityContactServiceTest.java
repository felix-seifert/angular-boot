package seifert.back.facility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import seifert.back.exceptions.EntityAlreadyExistsException;
import seifert.back.exceptions.EntityIDNotFoundException;
import seifert.back.exceptions.ErrorMessages;
import seifert.back.model.Facility;
import seifert.back.model.FacilityContact;
import seifert.back.model.repos.FacilityContactRepository;
import seifert.back.model.repos.FacilityRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class FacilityContactServiceTest {

    @Mock
    private UriComponentsBuilder uriComponentsBuilder;

    @Mock
    private UriComponents uriComponents;

    @Autowired
    private FacilityContactService facilityContactService;

    @MockBean
    private FacilityRepository facilityRepository;

    @MockBean
    private FacilityContactRepository facilityContactRepository;

    private static Facility facilityExpected1;

    private static List<FacilityContact> facilityContactListExpected;
    private static FacilityContact facilityContactExpected1;

    @BeforeAll
    public static void setup() {

        facilityExpected1 = Facility.builder()
                .id(1)
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();

        facilityContactExpected1 = FacilityContact.builder()
                .name("Interesting Person")
                .emailAddress("very@interesting.com").build();

        FacilityContact facilityContactExpected2 = FacilityContact.builder()
                .name("Responsible Woman")
                .telephoneNumber("+12 345 6789").build();

        facilityContactListExpected = Arrays.asList(facilityContactExpected1, facilityContactExpected2);
    }

    @Test
    public void getAllFacilityContactsForFacilityIDTest() {
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facilityExpected1));
        when(facilityContactRepository.findFacilityContactsByFacility(facilityExpected1))
                .thenReturn(facilityContactListExpected);
        ResponseEntity<List<FacilityContact>> actual = facilityContactService.getAllFacilityContactsForFacilityID(1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(facilityContactListExpected, actual.getBody());
    }

    @Test
    public void getAllFacilityContactsForFacilityIDTest_noFacilityFound() {
        when(facilityRepository.findById(1)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityContactService.getAllFacilityContactsForFacilityID(anyInt()));
        assertEquals(ErrorMessages.FACILITY_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void getAllFacilityContactsForFacilityIDTest_noContactsFound() {
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facilityExpected1));
        when(facilityContactRepository.findFacilityContactsByFacility(facilityExpected1))
                .thenReturn(new LinkedList<>());
        ResponseEntity<List<FacilityContact>> actual = facilityContactService.getAllFacilityContactsForFacilityID(1);
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertEquals(null, actual.getBody());
    }

    @Test
    public void getFacilityContactByIDTest() {
        when(facilityContactRepository.findById(1)).thenReturn(Optional.of(facilityContactExpected1));
        ResponseEntity<FacilityContact> actual = facilityContactService.getFacilityContactByID(1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(facilityContactExpected1, actual.getBody());
    }

    @Test
    public void getFacilityContactByIDTest_noFacilityContactFound() {
        when(facilityContactRepository.findById(anyInt())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityContactService.getFacilityContactByID(anyInt()));
        assertEquals(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void postFacilityContactForFacilityIDTest() throws URISyntaxException {
        URI location = new URI(String.format("http://localhost:8080/facilities/contacts/%d",
                facilityContactExpected1.getId()));
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facilityExpected1));
        when(facilityContactRepository
                .findFacilityContactByNameAndFacility(facilityContactExpected1.getName(), facilityExpected1))
                .thenReturn(Optional.empty());
        when(facilityContactRepository.save(facilityContactExpected1)).thenReturn(facilityContactExpected1);
        when(uriComponentsBuilder.path("/facilities/contacts/{contactID}")).thenReturn(uriComponentsBuilder);
        when(uriComponentsBuilder.buildAndExpand(facilityContactExpected1.getId())).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(location);

        ResponseEntity<String> actual = facilityContactService.postFacilityContactForFacilityID(
                facilityContactExpected1, 1, uriComponentsBuilder);

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(location, actual.getHeaders().getLocation());
    }

    @Test
    public void postFacilityContactForFacilityIDTest_noFacilityFound() {
        when(facilityRepository.findById(1)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(EntityIDNotFoundException.class, () -> facilityContactService
                .postFacilityContactForFacilityID(facilityContactExpected1, 1, uriComponentsBuilder));
        assertEquals(ErrorMessages.FACILITY_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void postFacilityContactForFacilityIDTest_contactAlreadyExists() {
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facilityExpected1));
        when(facilityContactRepository
                .findFacilityContactByNameAndFacility(facilityContactExpected1.getName(), facilityExpected1))
                .thenReturn(Optional.of(facilityContactExpected1));
        Throwable exception = assertThrows(EntityAlreadyExistsException.class, () -> facilityContactService
                .postFacilityContactForFacilityID(facilityContactExpected1, 1, uriComponentsBuilder));
        assertEquals(ErrorMessages.GIVEN_FACILITY_CONTACT_NAME_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    public void putFacilityContactTest() {
        FacilityContact newContact = FacilityContact.builder().id(1)
                .name(facilityContactExpected1.getName() + " new").emailAddress("changed@email.com").build();
        when(facilityContactRepository.findById(1)).thenReturn(Optional.of(facilityContactExpected1));
        when(facilityContactRepository.save(newContact)).thenReturn(newContact);

        ResponseEntity<FacilityContact> actual = facilityContactService.putFacilityContact(1, newContact);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(newContact, actual.getBody());
    }

    @Test
    public void putFacilityContactTest_noFacilityContactFound() {
        FacilityContact newContact = FacilityContact.builder().id(facilityContactExpected1.getId())
                .name(facilityContactExpected1.getName() + " new").emailAddress("changed@email.com").build();
        when(facilityContactRepository.findById(1)).thenReturn(Optional.empty());
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityContactService.putFacilityContact(1, newContact));
        assertEquals(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void deleteFacilityContactByIDTest() {
        when(facilityContactRepository.existsById(1)).thenReturn(true);
        ResponseEntity<FacilityContact> actual = facilityContactService.deleteFacilityContactByID(1);
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertEquals(null, actual.getBody());
    }

    @Test
    public void deleteFacilityContactByIDTest_noFacilityFound() {
        when(facilityContactRepository.existsById(1)).thenReturn(false);
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityContactService.deleteFacilityContactByID(1));
        assertEquals(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND, exception.getMessage());
    }
}
