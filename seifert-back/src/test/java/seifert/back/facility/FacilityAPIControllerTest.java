package seifert.back.facility;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class FacilityAPIControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @MockBean
    private FacilityRepository facilityRepository;

    private static List<Facility> facilityListExpected;
    private static Facility facilityExpected1;

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
    }

    @Test
    @Disabled
    public void getAllFacilitiesTest_givenUserDoesNotExist() {
        // implement when authentification for APIs exists
    }

    @Test
    public void getAllFacilitiesTest() {

        when(facilityRepository.findAll()).thenReturn(facilityListExpected);

        ResponseEntity<List<Facility>> actual = restTemplate.exchange(
                createLocalURLWithPort("/facilities/"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Facility>>() {});

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(actual.getBody().stream().allMatch(facility -> facilityListExpected.contains(facility)));
    }

    @Test
    public void getFacilityByIDTest() {

        when(facilityRepository.findById(1)).thenReturn(Optional.of(facilityExpected1));

        ResponseEntity<Facility> actual =
                restTemplate.getForEntity(createLocalURLWithPort("/facilities/1"), Facility.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertTrue(facilityExpected1.equals(actual.getBody()));
    }

    @Test
    @Disabled
    public void postFacilityTest() {
        // next step: implementing the test for the post API
    }

    private String createLocalURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
