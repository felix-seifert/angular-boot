import { Component, OnInit } from '@angular/core';
import { ApiDataService } from '../data/api-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Facility } from './facility';
import { FacilityContact } from './facility-contact';

@Component({
  selector: 'app-facility',
  templateUrl: './facility.component.html'
})
export class FacilityComponent implements OnInit {

  private facilityID;

  public facility: Facility;

  public facilityContacts: FacilityContact[];

  constructor(private route: ActivatedRoute, private router: Router, private apiDataService: ApiDataService) {
    this.route.params.subscribe(params => this.facilityID = params.id);
  }

  ngOnInit() {
    this.getFacilityByID();
    this.getAllFacilityContactsForFacilityID();
  }

  private getFacilityByID() {
    this.apiDataService.getFacilityByID(this.facilityID).subscribe(
      data => this.facility = data
    );
  }

  private getAllFacilityContactsForFacilityID() {
    this.apiDataService.getAllFacilityContactsForFacilityID(this.facilityID).subscribe(
      data => this.facilityContacts = data
    );
  }

  public deleteFacility() {
    this.apiDataService.deleteFacility(this.facility.id).subscribe(
      data => this.router.navigate(['/facilities/'])
    );
  }
}
