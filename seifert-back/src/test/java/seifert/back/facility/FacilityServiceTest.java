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
public class FacilityServiceTest {

    @Mock
    private UriComponentsBuilder uriComponentsBuilder;

    @Mock
    private UriComponents uriComponents;

    @Autowired
    private FacilityService facilityService;

    @MockBean
    private FacilityRepository facilityRepository;

    private static List<Facility> facilityListExpected;
    private static Facility facilityExpected1;

    @BeforeAll
    public static void setup() {

        facilityExpected1 = Facility.builder()
                .id(1)
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();

        Facility facilityExpected2 = Facility.builder()
                .name("Second Facility")
                .zipCode(22222)
                .city("Town 2").build();

        facilityListExpected = Arrays.asList(facilityExpected1, facilityExpected2);
    }

    @Test
    public void getAllFacilitiesTest() {
        when(facilityRepository.findAll()).thenReturn(facilityListExpected);
        ResponseEntity<List<Facility>> actual = facilityService.getAllFacilities();
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(facilityListExpected, actual.getBody());
    }

    @Test
    public void getAllFacilitiesTest_noFacilitiesFound() {
        when(facilityRepository.findAll()).thenReturn(new LinkedList<>());
        ResponseEntity<List<Facility>> actual = facilityService.getAllFacilities();
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertEquals(null, actual.getBody());
    }

    @Test
    public void getFacilityByIDTest() {
        when(facilityRepository.findById(1)).thenReturn(Optional.of(facilityExpected1));
        ResponseEntity<Facility> actual = facilityService.getFacilityByID(1);
        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(facilityExpected1, actual.getBody());
    }

    @Test
    public void getFacilityByIDTest_noFacilityFound() {
        when(facilityRepository.findById(anyInt())).thenReturn(Optional.empty());
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityService.getFacilityByID(anyInt()));
        assertEquals(ErrorMessages.FACILITY_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void postFacilityTest() throws URISyntaxException {
        URI location = new URI("http://localhost:8080/facilities/" + facilityExpected1.getId());
        when(facilityRepository.save(facilityExpected1)).thenReturn(facilityExpected1);
        when(uriComponentsBuilder.path("/facilities/{id}")).thenReturn(uriComponentsBuilder);
        when(uriComponentsBuilder.buildAndExpand(facilityExpected1.getId())).thenReturn(uriComponents);
        when(uriComponents.toUri()).thenReturn(location);

        ResponseEntity<Facility> actual = facilityService.postFacility(facilityExpected1, uriComponentsBuilder);

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(location, actual.getHeaders().getLocation());
        assertEquals(facilityExpected1, actual.getBody());
    }

    @Test
    public void postFacilityTest_facilityAlreadyExists() {
        when(facilityRepository.findFacilityByName(facilityExpected1.getName()))
                .thenReturn(Optional.ofNullable(facilityExpected1));
        Throwable exception = assertThrows(EntityAlreadyExistsException.class,
                () -> facilityService.postFacility(facilityExpected1, uriComponentsBuilder));
        assertEquals(ErrorMessages.GIVEN_FACILITY_NAME_ALREADY_EXISTS, exception.getMessage());
    }

    @Test
    public void putFacilityTest() {
        Facility newFacility = Facility.builder().id(facilityExpected1.getId())
                .name(facilityExpected1.getName() + "new").city("new").build();
        when(facilityRepository.existsById(1)).thenReturn(true);
        when(facilityRepository.save(newFacility)).thenReturn(newFacility);

        ResponseEntity<Facility> actual = facilityService.putFacility(1, newFacility);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(newFacility, actual.getBody());
    }

    @Test
    public void putFacilityTest_noFacilityFound() {
        Facility newFacility = Facility.builder().id(facilityExpected1.getId())
                .name(facilityExpected1.getName() + "new").city("new").build();
        when(facilityRepository.existsById(1)).thenReturn(false);
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityService.putFacility(1, newFacility));
        assertEquals(ErrorMessages.FACILITY_ID_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void deleteFacilityByIDTest() {
        when(facilityRepository.existsById(1)).thenReturn(true);
        ResponseEntity<Facility> actual = facilityService.deleteFacilityByID(1);
        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
        assertEquals(null, actual.getBody());
    }

    @Test
    public void deleteFacilityByIDTest_noFacilityFound() {
        when(facilityRepository.existsById(1)).thenReturn(false);
        Throwable exception = assertThrows(EntityIDNotFoundException.class,
                () -> facilityService.deleteFacilityByID(1));
        assertEquals(ErrorMessages.FACILITY_ID_NOT_FOUND, exception.getMessage());
    }
}
