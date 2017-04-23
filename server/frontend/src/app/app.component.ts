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
  prueba = "SIN RESPUESTA";

  constructor(private mHTTPService: HTTPService, public dialog: MdDialog) {
    this.USER_ID = "58fc617ca926e68850ea8427";
  }

  clicked(message: string){
    this.mHTTPService.sendUpdateRequestSMS(this.USER_ID, message).subscribe(data =>
      this.prueba = data);
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
      var count = 0;
      for (let atender of atenders) {
        this.mHTTPService.listUser(atender.$oid).subscribe(data => {
            this.loadContact(data);
            ++count;
            if (count === atenders.length) this.startObservablDrawingMarkers();
        });
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
      var httpServ = this.mHTTPService;
      var usID = this.USER_ID;
      var drawMarkersFunc = this.drawMarkers;
      var map = this.map;
      var markers = this.markers;
      setTimeout(function() {
        httpServ.sendRequestAtenders(usID).subscribe(data => {
          var coordinates = [];
          for (var i = 0; i < data.length; ++i) {
            if (data[i].latlong !== undefined) {
              var latlong = data[i].latlong.split(',');
              coordinates[coordinates.length] = {lat: latlong[0], lon: latlong[1]};
            }
          }
          drawMarkersFunc(map, markers, coordinates);
        });
      }, 5000);
  }

  //TODO Poner con información correcta de servidor
  drawMarkers(map, markers, data) {
    for (let marker of markers) {
      map.removeLayer(marker);
    }
    for (var i = 0; i < data.length; ++i) {
      markers.push(L.marker([data[i].lat, data[i].lon]).addTo(map));
    }
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
