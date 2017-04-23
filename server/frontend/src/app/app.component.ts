import { Component } from '@angular/core';
import { Observable } from 'rxjs/Rx';
import { MdDialog, MdDialogRef } from '@angular/material';
import 'leaflet';
import { HTTPService } from './services/HTTPService';
import { Contact } from './models/contact';
import { User } from './models/user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  USER_ID: string;
  user = new User();
  markers = new Array<L.Marker>();
  map: L.Map;

  constructor(private mHTTPService: HTTPService, public dialog: MdDialog) {
    this.USER_ID = "58fc28d4a926e67e47ebd7b1";
  }

  clicked(message: string){
    this.mHTTPService.sendUpdateRequestSMS(message).subscribe(data =>
        "");
    this.startObservablDrawingMarkers();
  }

  ngOnInit(): void {
    this.map = L.map('map')
      .setView([55.7649551, 12.5202844], 13);

    L.tileLayer('http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> &copy; <a href="http://cartodb.com/attributions">CartoDB</a>',
      subdomains: 'abcd',
      maxZoom: 19
    }).addTo(this.map);

    this.mHTTPService.listUser(this.USER_ID).subscribe(data =>
        this.loadUser(data));
  }

  loadUser(data){
      this.user = new User();
      this.user.contactsArray = new Array<Contact>();
      this.user.id = data._id.$oid;

      var atenders = data.atenders;
      for (let atender of atenders) {
        this.mHTTPService.listUser(atender.$oid).subscribe(data =>
            this.loadContact(data));
      }
  }

  loadContact(data){
    var contact = new Contact();
    contact.id = data._id.$oid;
    contact.name = data.firstname;
    contact.surname = data.lastname;
    contact.phone = data.phone;
    contact.email = data.email;
    contact.latitude = 0;
    contact.longitude = 0;

    this.user.contactsArray.push(contact);
  }

  startObservablDrawingMarkers() {
      Observable.interval(5000).subscribe(x => {
        this.mHTTPService.sendUpdateRequestLocation().subscribe(data =>
            this.drawMarkers(data));
      });
  }

  //TODO Poner con información correcta de servidor
  drawMarkers(data) {
    for (let marker of this.markers) {
      this.map.removeLayer(marker);
    }
    this.markers.push(L.marker([55.768028, 12.503128]).addTo(this.map));
    this.markers.push(L.marker([55.772319, 12.508964]).addTo(this.map));
    this.markers.push(L.marker([55.761173, 12.522551]).addTo(this.map));
  }

  addGuestToEvent(arrayCurrentGuestsID, newGuestID){
    var atendersArray = [];
    for (let currentGuest of arrayCurrentGuestsID) {
      atendersArray.push({
          "id": currentGuest.id
      });
    }

    atendersArray.push({
        "id": newGuestID.$oid
    });

    var dataToSend = {
    	"date": "2017-04-22T21:53:00.283Z",
    	"atenders": atendersArray
    }

    this.mHTTPService.addAtender(this.user.id, dataToSend).subscribe(data =>
      location.reload());
  }

  openDialog() {
    let dialogRef = this.dialog.open(DialogResult);
    dialogRef.afterClosed().subscribe(result => {
      var result = result.split("#");

      if (result.length == 4) {
        var name = result[0];
        var surname = result[1];
        var email = result[2];
        var phone = result[3];

        var contact = {
          "username": name + surname,
          "firstname": name,
          "lastname": surname,
          "email": email,
          "phone": phone
        }

        //Enviar a backend para crear Contacto y refrescar pagina
        this.mHTTPService.addContact(contact).subscribe(data =>
          this.addGuestToEvent(this.user.contactsArray, data._id));
      }
    });
  }
}

@Component({
  selector: 'dialog-result',
  templateUrl: './dialog-result.html',
})
export class DialogResult {
  constructor(public dialogRef: MdDialogRef<DialogResult>) {}
}
