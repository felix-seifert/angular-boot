import { Component, OnInit } from '@angular/core';
import { ApiDataService } from '../data/api-data.service';

@Component({
  selector: 'app-facilities',
  templateUrl: './facilities.component.html'
})
export class FacilitiesComponent implements OnInit {

  facilities$: Object;

  constructor(private apiData: ApiDataService) { }

  ngOnInit() {
    this.apiData.getAllFacilities().subscribe(data => {
      this.facilities$ = data;
    });
  }

}
