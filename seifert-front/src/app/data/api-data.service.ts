import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Facility } from '../facility/facility';
import { FacilityContact } from '../facility/facility-contact';

@Injectable()
export class ApiDataService {

  private HOST = '//localhost:8080';

  constructor(private httpClient: HttpClient) { }

  getAllFacilities(): Observable<Facility[]> {
    return this.httpClient.get<Facility[]>(this.HOST + '/facilities/');
  }

  getFacilityByID(facilityID): Observable<Facility> {
    return this.httpClient.get<Facility>(this.HOST + '/facilities/' + facilityID);
  }

  postFacility(facility: Facility): Observable<Facility> {
    return this.httpClient.post<Facility>(this.HOST + '/facilities/', facility);
  }

  deleteFacility(facilityID): Observable<any> {
    return this.httpClient.delete(this.HOST + '/facilities/' + facilityID);
  }

  getAllFacilityContactsForFacilityID(facilityID): Observable<FacilityContact[]> {
    return this.httpClient.get<FacilityContact[]>(this.HOST + '/facilities/' + facilityID + '/contacts');
  }
}
