import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class ApiDataService {

  constructor(private http: HttpClient) { }

  getAllFacilities(): Observable<any> {
    return this.http.get('http://localhost:8080/facilities/');
  }

  getFacilityByID(facilityID): Observable<any> {
    return this.http.get('http://localhost:8080/facilities/' + facilityID);
  }

  getAllFacilityContactsForFacilityID(facilityID): Observable<any> {
    return this.http.get('http://localhost:8080/facilities/' + facilityID + '/contacts');
  }
}
