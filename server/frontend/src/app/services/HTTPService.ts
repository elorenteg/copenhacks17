import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class HTTPService {
  URL_ADD_CONTACT: string;
  URL_UPDATE_LOCATIONS_SMS: string;
  URL_UPDATE_LOCATIONS_LOCATION: string;

  constructor ( private http: Http ) {
    this.URL_ADD_CONTACT = "";
    this.URL_UPDATE_LOCATIONS_SMS = "";
    this.URL_UPDATE_LOCATIONS_LOCATION = "";
  }

  addContact(contact){
    var url = this.URL_ADD_CONTACT + "";

    return this.http.get(url)
      //.map((res:Response) => res.json());
        .map((res:Response) => "HI");
  }

  sendUpdateRequestSMS(message) {
    var url = this.URL_UPDATE_LOCATIONS_SMS + message;

    return this.http.get(url)
      //.map((res:Response) => res.json());
        .map((res:Response) => "HI");
  }

  sendUpdateRequestLocation() {
    var url = this.URL_UPDATE_LOCATIONS_LOCATION;

    return this.http.get(url)
      //.map((res:Response) => res.json());
        .map((res:Response) => "HI");
  }
}
