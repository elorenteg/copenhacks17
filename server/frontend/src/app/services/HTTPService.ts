import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class HTTPService {
  URL_ADD_CONTACT: string;
  URL_UPDATE_LOCATIONS_SMS: string;
  URL_UPDATE_LOCATIONS_LOCATION: string;
  URL_EVENT_ATENDERS: string;

  constructor ( private http: Http ) {
    this.URL_ADD_CONTACT = "";
    this.URL_UPDATE_LOCATIONS_SMS = "http://6c6a0657.ngrok.io";
    this.URL_UPDATE_LOCATIONS_LOCATION = "";
    this.URL_EVENT_ATENDERS = "http://6c6a0657.ngrok.io";
  }

  addContact(contact){
    var url = this.URL_ADD_CONTACT + "";

    return this.http.get(url)
      //.map((res:Response) => res.json());
        .map((res:Response) => "HI");
  }

  sendUpdateRequestSMS(userID, message) {
    var url = this.URL_UPDATE_LOCATIONS_SMS + "/user/" + userID + "/send";
    console.log(url);

    var body = {"message": message};

    return this.http.post(url, body)
      //.map((res:Response) => res.json());
      .map((res:Response) => "HI");
  }

  sendUpdateRequestLocation() {
    var url = this.URL_UPDATE_LOCATIONS_LOCATION;

    return this.http.get(url)
      //.map((res:Response) => res.json());
        .map((res:Response) => "HI");
  }

  sendRequestAtenders(userID) {
    var url = this.URL_EVENT_ATENDERS + "/user/" + userID + "/atenders";
    console.log(url);

    return this.http.get(url)
        .map((res:Response) => res.json());
  }
}
