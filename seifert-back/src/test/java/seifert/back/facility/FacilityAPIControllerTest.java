package seifert.back.facility;

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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    @Disabled
    public void getAllFacilitiesTest_givenUserDoesNotExist() {
        // implement when authentification for APIs exists
    }

    @Test
    public void getAllFacilitiesTest() {

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

        List<Facility> facilityList = Arrays.asList(facilityExpected1, facilityExpected2);
        when(facilityRepository.findAll()).thenReturn(facilityList);

        Iterable<Facility> expected = facilityRepository.findAll();

        List<Facility> actual =
                restTemplate.getForObject(createLocalURLWithPort("/facilities/"), List.class);
        
        boolean same = true;
        for(Facility facility : expected) {
            if(facility.equals(actual.iterator().next())) {
                same = false;
            }
        }

        assertEquals(true, same);
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
