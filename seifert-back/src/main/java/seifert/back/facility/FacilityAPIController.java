package seifert.back.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import seifert.back.exceptions.EntityAlreadyExistsException;
import seifert.back.exceptions.EntityIDNotFoundException;
import seifert.back.exceptions.ErrorMessages;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/facilities")
@CrossOrigin(origins = "http://localhost:4200")
public class FacilityAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityAPIController.class);

    @Autowired
    private FacilityRepository facilityRepository;

    @GetMapping("/")
    public HttpEntity<List<Facility>> getAllFacilities() {
        LOGGER.info("Get all Facilities");
        List<Facility> facilityList = facilityRepository.findAll();

        ResponseEntity<List<Facility>> response;
        if(facilityList.isEmpty()) { response = new ResponseEntity(HttpStatus.NO_CONTENT); }
        else { response = new ResponseEntity(facilityList, HttpStatus.OK); }

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityByID(@PathVariable Integer id) {
        LOGGER.info("Get Facility with id={}", id);
        Optional<Facility> facilityFound = facilityRepository.findById(id);
        if(!facilityFound.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(facilityFound.get(), HttpStatus.OK);
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
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }
}
