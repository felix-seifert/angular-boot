import { Component, OnInit } from '@angular/core';
import { ApiDataService } from '../data/api-data.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-facility',
  templateUrl: './facility.component.html'
})
export class FacilityComponent implements OnInit {

  facility$: Object;

  constructor(private route: ActivatedRoute, private apiData: ApiDataService) {
    this.route.params.subscribe(params => this.facility$ = params.id);
  }

  ngOnInit() {
    this.apiData.getFacilityByID(this.facility$).subscribe(
      data => this.facility$ = data
    );
  }

}
