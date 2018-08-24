package seifert.back.facility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import seifert.back.model.Facility;
import seifert.back.model.FacilityContact;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class FacilityAPIControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private FacilityService facilityService;

    @MockBean
    private FacilityContactService facilityContactService;

    private static List<Facility> facilityListExpected;
    private static Facility facilityExpected1;

    private static List<FacilityContact> facilityContactListExpected;
    private static FacilityContact facilityContactExpected1;

    @BeforeAll
    public static void setup() {

        facilityExpected1 = Facility.builder()
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

        facilityContactExpected1 = FacilityContact.builder()
                .name("Interesting Person")
                .emailAddress("very@interesting.com").build();

        FacilityContact facilityContactExpected2 = FacilityContact.builder()
                .name("Responsible Woman")
                .telephoneNumber("+12 345 6789").build();

        facilityContactListExpected = Arrays.asList(facilityContactExpected1, facilityContactExpected2);
    }

    @Test
    public void getAllFacilitiesTest() {
        when(facilityService.getAllFacilities()).thenReturn(new ResponseEntity(facilityListExpected, HttpStatus.OK));

        ResponseEntity<List<Facility>> actual = restTemplate.exchange(
                createLocalURLWithPort("/facilities/"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Facility>>() {});

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, actual.getHeaders().getContentType());
        assertEquals(facilityListExpected, actual.getBody());
    }

    @Test
    public void getFacilityByIDTest() {
        when(facilityService.getFacilityByID(1)).thenReturn(new ResponseEntity<>(facilityExpected1, HttpStatus.OK));

        ResponseEntity<Facility> actual =
                restTemplate.getForEntity(createLocalURLWithPort("/facilities/1"), Facility.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, actual.getHeaders().getContentType());
        assertTrue(facilityExpected1.equals(actual.getBody()));
    }

    @Test
    public void postFacilityTest() throws URISyntaxException {
        URI location = new URI("http://localhost:8080/facilities/" + facilityExpected1.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        when(facilityService.postFacility(eq(facilityExpected1), any()))
                .thenReturn(new ResponseEntity<>(httpHeaders, HttpStatus.CREATED));

        ResponseEntity<String> actual = restTemplate.postForEntity(createLocalURLWithPort("/facilities/"),
                facilityExpected1, String.class);

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(location, actual.getHeaders().getLocation());
    }

    @Test
    public void putFacilityTest() {
        Facility newFacility = Facility.builder().id(1)
                .name(facilityExpected1.getName() + "new").city("new").build();
        when(facilityService.putFacility(1, newFacility))
                .thenReturn(new ResponseEntity<>(newFacility, HttpStatus.OK));

        ResponseEntity<Facility> actual = restTemplate.exchange(createLocalURLWithPort("facilities/1"),
                HttpMethod.PUT, new HttpEntity<>(newFacility), Facility.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, actual.getHeaders().getContentType());
        assertEquals(newFacility, actual.getBody());
    }

    @Test
    public void deleteFacilityByIDTest() {
        when(facilityService.deleteFacilityByID(1)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Facility> actual = restTemplate.exchange(createLocalURLWithPort("/facilities/1"),
                HttpMethod.DELETE, HttpEntity.EMPTY, Facility.class);

        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    @Test
    public void getAllFacilityContactsForFacilityIDTest() {
        when(facilityContactService.getAllFacilityContactsForFacilityID(1))
                .thenReturn(new ResponseEntity(facilityContactListExpected, HttpStatus.OK));

        ResponseEntity<List<FacilityContact>> actual = restTemplate.exchange(
                createLocalURLWithPort("/facilities/1/contacts"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<FacilityContact>>() {});

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, actual.getHeaders().getContentType());
        assertEquals(facilityContactListExpected, actual.getBody());
    }

    @Test
    public void getFacilityContactByIDTest() {
        when(facilityContactService.getFacilityContactByID(1))
                .thenReturn(new ResponseEntity<>(facilityContactExpected1, HttpStatus.OK));

        ResponseEntity<FacilityContact> actual = restTemplate
                .getForEntity(createLocalURLWithPort("/facilities/contacts/1"), FacilityContact.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, actual.getHeaders().getContentType());
        assertTrue(facilityContactExpected1.equals(actual.getBody()));
    }

    @Test
    public void postFacilityContactForFacilityIDTest() throws URISyntaxException {
        URI location = new URI("http://localhost:8080/facilities/1/contacts/1" + facilityContactExpected1.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(location);
        when(facilityContactService.postFacilityContactForFacilityID(eq(facilityContactExpected1), eq(1), any()))
                .thenReturn(new ResponseEntity<>(httpHeaders, HttpStatus.CREATED));

        ResponseEntity<String> actual = restTemplate.postForEntity(createLocalURLWithPort("/facilities/1/contacts"),
                facilityContactExpected1, String.class);

        assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        assertEquals(location, actual.getHeaders().getLocation());
    }

    @Test
    public void putFacilityContactTest() {
        FacilityContact newContact = FacilityContact.builder().id(facilityContactExpected1.getId())
                .name(facilityContactExpected1.getName() + " new").emailAddress("changed@email.com").build();
        when(facilityContactService.putFacilityContact(1, newContact))
                .thenReturn(new ResponseEntity<>(newContact, HttpStatus.OK));

        ResponseEntity<FacilityContact> actual = restTemplate.exchange(
                createLocalURLWithPort("facilities/contacts/1"),
                HttpMethod.PUT, new HttpEntity<>(newContact), FacilityContact.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, actual.getHeaders().getContentType());
        assertEquals(newContact, actual.getBody());
    }

    @Test
    public void deleteFacilityContactByIDTest() {
        when(facilityContactService.deleteFacilityContactByID(1))
                .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<FacilityContact> actual = restTemplate.exchange(
                createLocalURLWithPort("/facilities/contacts/1"),
                HttpMethod.DELETE, HttpEntity.EMPTY, FacilityContact.class);

        assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
    }

    private String createLocalURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
