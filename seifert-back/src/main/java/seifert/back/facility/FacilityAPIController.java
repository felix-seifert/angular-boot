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
import seifert.back.model.FacilityContact;
import seifert.back.model.repos.FacilityContactRepository;
import seifert.back.model.repos.FacilityRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/facilities")
@CrossOrigin(origins = "http://localhost:4200") // Exposes the APIs only to localhost on port 4200.
public class FacilityAPIController {            // Disable CrossOrigin  for access via other programmes (e.g. Postman).

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityAPIController.class);

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private FacilityContactRepository facilityContactRepository;

    @GetMapping("")
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

    @PostMapping("")
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

    @GetMapping("/{facilityID}/contacts")
    public HttpEntity<List<FacilityContact>> getAllFacilityContactsForFacilityID(@PathVariable Integer facilityID) {
        LOGGER.info("Get all contacts for facilityID={}", facilityID);

        Optional<Facility> facility = facilityRepository.findById(facilityID);

        if(!facility.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }

        List<FacilityContact> contactList =
                facilityContactRepository.findFacilityContactsByFacility(facility.get());

        ResponseEntity<List<FacilityContact>> response;
        if(contactList.isEmpty()) { response = new ResponseEntity(HttpStatus.NO_CONTENT); }
        else { response = new ResponseEntity(contactList, HttpStatus.OK); }

        return response;
    }

    @PostMapping("/{facilityID}/contacts")
    public ResponseEntity<String> postFacilityContactForFacilityID(@RequestBody FacilityContact facilityContact,
                                                                   @PathVariable Integer facilityID,
                                                                   UriComponentsBuilder builder) {

        LOGGER.info("Create FacilityContact: {}", facilityContact);

        Optional<Facility> facility = facilityRepository.findById(facilityID);
        if(!facility.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }

        if(facilityContactRepository.findFacilityContactByNameAndFacility(facilityContact.getName(),
                facility.get()).isPresent()) {
            LOGGER.error(ErrorMessages.GIVEN_FACILITY_CONTACT_NAME_ALREADY_EXISTS);
            throw new EntityAlreadyExistsException(ErrorMessages.GIVEN_FACILITY_CONTACT_NAME_ALREADY_EXISTS);
        }

        FacilityContact facilityContactWithID = FacilityContact.builder()
                .name(facilityContact.getName())
                .emailAddress(facilityContact.getEmailAddress())
                .telephoneNumber(facilityContact.getTelephoneNumber())
                .facility(facility.get())
                .build();


        facilityContactRepository.save(facilityContactWithID);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(builder.path("/facilities/{id}/").buildAndExpand(facilityID).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }
}
