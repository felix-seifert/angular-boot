package seifert.back.facility;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class FacilityAPIControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacilityRepository facilityRepository;

    private static List<Facility> facilityListExpected;
    private static Optional<Facility> facilityOptionalExpected;

    @Test
    @Disabled
    public void getAllFacilitiesTest_givenUserDoesNotExist() {
        // implement when authentification for APIs exists
    }

    @BeforeAll
    public static void setup() {

        Facility facilityExpected1 = Facility.builder()
                .id(1)
                .name("Facility 1")
                .street("First Street")
                .houseNumber(11)
                .zipCode(11111)
                .city("Important City").build();

        Facility facilityExpected2 = Facility.builder()
                .id(2)
                .name("Second Facility")
                .zipCode(22222)
                .city("Town 2").build();

        facilityListExpected = Arrays.asList(facilityExpected1, facilityExpected2);
        facilityOptionalExpected = Optional.of(facilityExpected1);
    }

    @Test
    public void getAllFacilitiesTest() {

        when(facilityRepository.findAll()).thenReturn(facilityListExpected);

        List<Facility> actual =
                restTemplate.getForObject(createLocalURLWithPort("/facilities/"), List.class);

        boolean same = true;
        for(Facility facility : facilityListExpected) {
            if(facility.equals(actual.iterator().next())) {
                same = false;
            }
        }

        assertEquals(true, same);
    }

    @Test
    public void getFacilityByIDTest() throws IOException {

        when(facilityRepository.findById(1)).thenReturn(facilityOptionalExpected);

        String actual =
                restTemplate.getForObject(createLocalURLWithPort("facilities/1"), String.class);

        Facility facilityActual = objectMapper.readValue(actual, Facility.class);

        assertEquals(true, facilityOptionalExpected.get().equals(facilityActual));
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
