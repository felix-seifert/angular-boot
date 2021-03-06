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
import seifert.back.model.FacilityContact;
import seifert.back.model.repos.FacilityContactRepository;
import seifert.back.model.repos.FacilityRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityContactService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityAPIController.class);

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private FacilityContactRepository facilityContactRepository;

    protected ResponseEntity<List<FacilityContact>> getAllFacilityContactsForFacilityID(Integer facilityID) {
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

    protected ResponseEntity<FacilityContact> getFacilityContactByID(Integer id) {
        LOGGER.info("Get Contact with id={}", id);
        Optional<FacilityContact> contactFound = facilityContactRepository.findById(id);
        if(!contactFound.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND);
        }
        return new ResponseEntity<>(contactFound.get(), HttpStatus.OK);
    }

    protected ResponseEntity<String> postFacilityContactForFacilityID(FacilityContact facilityContact,
                                                                   Integer facilityID,
                                                                   UriComponentsBuilder builder) {
        LOGGER.info("Create FacilityContact: {}", facilityContact);

        Optional<Facility> facility = facilityRepository.findById(facilityID);
        if (!facility.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_ID_NOT_FOUND);
        }

        if (facilityContactRepository.findFacilityContactByNameAndFacility(facilityContact.getName(),
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
        httpHeaders.setLocation(builder.path("/facilities/contacts/{contactID}")
                .buildAndExpand(facilityContactWithID.getId()).toUri());
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    protected ResponseEntity<FacilityContact> putFacilityContact(Integer id, FacilityContact contact)
            throws EntityIDNotFoundException {
        LOGGER.info("Update FacilityContact with id={}", id);

        Optional<FacilityContact> oldContact = facilityContactRepository.findById(id);

        if(!oldContact.isPresent()) {
            LOGGER.error(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND);
        }

        FacilityContact newContact = FacilityContact.builder().id(id).name(contact.getName())
                .emailAddress(contact.getEmailAddress()).telephoneNumber(contact.getTelephoneNumber())
                .facility(oldContact.get().getFacility()).build();

        facilityContactRepository.save(newContact);

        return new ResponseEntity<>(newContact, HttpStatus.OK);
    }

    protected ResponseEntity<FacilityContact> deleteFacilityContactByID(Integer id) throws EntityIDNotFoundException{
        LOGGER.info("Delete FacilityContact with id={}", id);

        if(!facilityContactRepository.existsById(id)) {
            LOGGER.error(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND);
            throw new EntityIDNotFoundException(ErrorMessages.FACILITY_CONTACT_ID_NOT_FOUND);
        }

        facilityContactRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
