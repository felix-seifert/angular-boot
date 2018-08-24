package seifert.back.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import seifert.back.exceptions.EntityAlreadyExistsException;
import seifert.back.exceptions.EntityIDNotFoundException;
import seifert.back.exceptions.ErrorMessages;
import seifert.back.model.Facility;
import seifert.back.model.repos.FacilityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityService.class);

    @Autowired
    private FacilityRepository facilityRepository;

    protected ResponseEntity<List<Facility>> getAllFacilities() {
        LOGGER.info("Get all Facilities");
        List<Facility> facilityList = facilityRepository.findAll();

        ResponseEntity<List<Facility>> response;
        if(facilityList.isEmpty()) { response = new ResponseEntity(HttpStatus.NO_CONTENT); }
        else { response = new ResponseEntity(facilityList, HttpStatus.OK); }

        return response;
    }

    protected ResponseEntity<Facility> getFacilityByID(Integer id) throws EntityIDNotFoundException {
        LOGGER.info("Get Facility with id={}", id);
        Optional<Facility> facilityFound = facilityRepository.findById(id);
        if(!facilityFound.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(facilityFound.get(), HttpStatus.OK);
    }

    protected ResponseEntity<String> postFacility(Facility facility, UriComponentsBuilder builder)
            throws EntityAlreadyExistsException {
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

    protected ResponseEntity<Facility> putFacility(Integer id, Facility facility) throws EntityIDNotFoundException {
        LOGGER.info("Update Facility with id={}", id);

        if(!facilityRepository.existsById(id)) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }

        Facility newFacility = Facility.builder().id(id).name(facility.getName()).street(facility.getStreet())
                .houseNumber(facility.getHouseNumber()).zipCode(facility.getZipCode()).city(facility.getCity()).build();

        facilityRepository.save(newFacility);

        return new ResponseEntity<>(newFacility, HttpStatus.OK);
    }

    protected ResponseEntity<Facility> deleteFacilityByID(Integer id) throws EntityIDNotFoundException {
        LOGGER.info("Delete Facility with id={}", id);

        if(!facilityRepository.existsById(id)) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }

        facilityRepository.deleteById(id);

        return new ResponseEntity<Facility>(HttpStatus.NO_CONTENT);
    }
}
