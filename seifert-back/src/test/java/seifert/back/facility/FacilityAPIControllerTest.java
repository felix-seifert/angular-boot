package seifert.back.facility;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
public class FacilityAPIControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FacilityRepository facilityRepository;

    @Test
    @Disabled
    public void getAllFacilitiesTest_givenUserDoesNotExist() {
        // implement when authentification for APIs exists
    }

    @Test
    public void getAllFacilitiesTest() {

        Iterable<Facility> expected = facilityRepository.findAll();

        Iterable<Facility> actual =
                restTemplate.getForObject(createLocalURLWithPort("/facilities/"), Iterable.class);
        boolean same = true;
        for(Facility facility : expected) {
            if(facility.equals(actual.iterator().next())) {
                same = false;
            }
        }

        assertEquals(true, same);
    }

    private String createLocalURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
