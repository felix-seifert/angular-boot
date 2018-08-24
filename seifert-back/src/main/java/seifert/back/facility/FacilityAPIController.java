package seifert.back.facility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import seifert.back.model.Facility;
import seifert.back.model.FacilityContact;

import java.util.List;

@RestController
@RequestMapping("/facilities")
@CrossOrigin(origins = "http://localhost:4200") // Exposes the APIs only to localhost on port 4200.
public class FacilityAPIController {            // Disable CrossOrigin  for access via other programmes (e.g. Postman).

    private static final Logger LOGGER = LoggerFactory.getLogger(FacilityAPIController.class);

    @Autowired
    private FacilityService facilityService;

    @Autowired
    private FacilityContactService facilityContactService;

    @GetMapping("")
    public ResponseEntity<List<Facility>> getAllFacilities() {
        return facilityService.getAllFacilities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Facility> getFacilityByID(@PathVariable Integer id) {
        return facilityService.getFacilityByID(id);
    }

    @PostMapping("")
    public ResponseEntity<String> postFacility(@RequestBody Facility facility, UriComponentsBuilder builder) {
        return facilityService.postFacility(facility, builder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facility> putFacility(@PathVariable Integer id, @RequestBody Facility facility) {
        return facilityService.putFacility(id, facility);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Facility> deleteFacilityByID(@PathVariable Integer id) {
        return  facilityService.deleteFacilityByID(id);
    }

    @GetMapping("/{facilityID}/contacts")
    public ResponseEntity<List<FacilityContact>> getAllFacilityContactsForFacilityID(@PathVariable Integer facilityID) {
        return facilityContactService.getAllFacilityContactsForFacilityID(facilityID);
    }

    @GetMapping("/contacts/{id}")
    public ResponseEntity<FacilityContact> getFacilityContactByID(@PathVariable Integer id) {
        return facilityContactService.getFacilityContactByID(id);
    }

    @PostMapping("/{facilityID}/contacts")
    public ResponseEntity<String> postFacilityContactForFacilityID(@RequestBody FacilityContact facilityContact,
                                                                   @PathVariable Integer facilityID,
                                                                   UriComponentsBuilder builder) {
        return facilityContactService.postFacilityContactForFacilityID(facilityContact, facilityID, builder);
    }
}
