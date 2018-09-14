import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { MenuComponent } from './menu/menu.component';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { FacilitiesComponent } from './facilities/facilities.component';
import { FacilityComponent } from './facility/facility.component';
import { FacilityAddComponent } from './facility-add/facility-add.component';

import { ApiDataService } from './data/api-data.service';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    MenuComponent,
    ContactComponent,
    HomeComponent,
    FacilitiesComponent,
    FacilityComponent,
    FacilityAddComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [ApiDataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
