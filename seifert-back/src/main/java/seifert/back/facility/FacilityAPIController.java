package seifert.back.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import seifert.back.facility.exceptions.EntityAlreadyExistsException;
import seifert.back.facility.exceptions.ErrorMessages;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/facilities")
public class FacilityAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityAPIController.class);

    @Autowired
    private FacilityRepository facilityRepository;

    @GetMapping("/")
    public List<Facility> getAllFacilities() {
        LOGGER.info("Get all Facilities");
        return facilityRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Facility> getFacilityByID(@PathVariable Integer id) {
        LOGGER.info("Get Facility with id= {}", id);
        return facilityRepository.findById(id);
    }

    @PostMapping("/")
    public ResponseEntity<String> postFacility(@RequestBody Facility facility, UriComponentsBuilder builder) {
        LOGGER.info("Create Facility: {}", facility);

        if(facilityRepository.findFacilityByName(facility.getName()).isPresent()) {
            LOGGER.error(ErrorMessages.GIVEN_FACILITY_NAME_ALREADY_EXISTS);
            throw new EntityAlreadyExistsException(ErrorMessages.GIVEN_FACILITY_NAME_ALREADY_EXISTS);
        }

        facilityRepository.save(facility);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(builder.path("/facilities/{id}").buildAndExpand(facility.getId()).toUri());
        return new ResponseEntity<String>(httpHeaders, HttpStatus.CREATED);
    }
}
