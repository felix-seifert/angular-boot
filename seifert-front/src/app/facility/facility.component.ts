import { Component, OnInit } from '@angular/core';
import { ApiDataService } from '../data/api-data.service';
import { ActivatedRoute } from '@angular/router';
import { Facility } from './facility';
import { FacilityContact } from './facility-contact';

@Component({
  selector: 'app-facility',
  templateUrl: './facility.component.html'
})
export class FacilityComponent implements OnInit {


  public facility: Facility;

  constructor(private route: ActivatedRoute, private apiDataService: ApiDataService) {
    this.route.params.subscribe(params => this.facility = params.id);
  public facilityContacts: FacilityContact[];
  }

  ngOnInit() {
    this.getFacilityByID();
    this.getAllFacilityContactsForFacilityID();
  }

  private getFacilityByID() {
    this.apiDataService.getFacilityByID(this.facility).subscribe(
      data => this.facility = data
    );
  }

  private getAllFacilityContactsForFacilityID() {
    this.apiDataService.getAllFacilityContactsForFacilityID(this.facility).subscribe(
      data => this.facilityContacts = data
    );
  }
}
