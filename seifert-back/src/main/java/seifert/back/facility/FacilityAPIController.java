package seifert.back.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

@RestController
@RequestMapping("/facilities")
public class FacilityAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityAPIController.class);

    @Autowired
    private FacilityRepository facilityRepository;

    @GetMapping("/")
    public Iterable<Facility> getAllFacilities() {
        LOGGER.info("getAllFacilities() called");
        return facilityRepository.findAll();
    }

    @PostMapping("/")
    public void postFacility(@RequestParam(value = "name") String name,
                             @RequestParam(value = "street") String street,
                             @RequestParam(value = "houseNumber") int houseNumber,
                             @RequestParam(value = "zipCode") int zipCode,
                             @RequestParam(value = "city") String city) {

        LOGGER.info("postFacility(params) called");

        facilityRepository.save(Facility.builder()
                .name(name)
                .street(street)
                .houseNumber(houseNumber)
                .zipCode(zipCode)
                .city(city)
                .build());
    }
}
