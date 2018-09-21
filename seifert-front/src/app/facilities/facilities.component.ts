import { Component, OnInit } from '@angular/core';
import { ApiDataService } from '../data/api-data.service';
import { Facility } from '../facility/facility';

@Component({
  selector: 'app-facilities',
  templateUrl: './facilities.component.html'
})
export class FacilitiesComponent implements OnInit {

  public facilities: Facility[];

  constructor(private apiDataService: ApiDataService) { }

  ngOnInit() {
    this.getAllFacilities();
  }

  private getAllFacilities() {
    this.apiDataService.getAllFacilities().subscribe(
      data => this.facilities = data
    );
  }
}
