import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';

@Injectable()
export class HTTPService {
  URL_UPDATE_LOCATIONS_MESSAGE: string;
  constructor ( private http: Http ) {
    this.URL_UPDATE_LOCATIONS_MESSAGE = "";
  }

  sendUpdateRequest(message) {
    var url = this.URL_UPDATE_LOCATIONS_MESSAGE + message;

    return this.http.get(url)
      .map((res:Response) => message);
      //.map((res:Response) => res.json());
  }
}
