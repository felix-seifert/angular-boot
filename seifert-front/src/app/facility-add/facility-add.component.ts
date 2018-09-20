import { Component, Input, OnInit } from '@angular/core';
import { ApiDataService } from '../data/api-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Facility } from '../facility/facility';

@Component({
  selector: 'app-facility-add',
  templateUrl: './facility-add.component.html',
  styleUrls: ['./facility-add.component.scss']
})
export class FacilityAddComponent implements OnInit {

  constructor(private route: ActivatedRoute, private router: Router, private apiDataService: ApiDataService) { }

  ngOnInit() {
  }

  private postFacility(name: string, street: string, houseNumber, zipCode, city: string) {
    name = name.trim();
    street = street.trim();
    houseNumber = houseNumber.trim();
    zipCode = zipCode.trim();
    city = city.trim();
    const facilityNew: Facility = { name, street, houseNumber, zipCode, city } as Facility;

    this.apiDataService.postFacility(facilityNew).subscribe(
      facility => this.router.navigate(['/facilities/details/' + facility.id]));
  }
}
