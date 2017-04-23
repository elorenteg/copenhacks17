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
  title = 'app works!';
  items = ['pepe', 'popo'];
  user = new User();
  contacts = new Array<Contact>();
  markers = new Array<L.Marker>();
  map: L.Map;
  prueba = "SIN RESPUESTA";
  userID = "58fbc23ba926e6737f157ec6";

  constructor(private mHTTPService: HTTPService, public dialog: MdDialog) {}

  clicked(message: string){
    this.mHTTPService.sendUpdateRequestSMS(this.userID, message).subscribe(data =>
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

    this.mHTTPService.sendUpdateRequestLocation().subscribe(data =>
        this.loadContacts(data));

    var contact1 = new Contact();
    contact1.id = 0;
    contact1.name = "Marc";
    contact1.surname = "Vila";
    contact1.phone = "(+34) 689 754 378";
    contact1.email = "marcvilagomez@gmail.com";
    contact1.latitude = 55.768028;
    contact1.longitude = 12.503128;

    var contact2 = new Contact();
    contact2.id = 1;
    contact2.name = "Ester";
    contact2.surname = "Lorente";
    contact2.phone = "(+34) 657 654 356";
    contact2.email = "esterlorente@gmail.com";
    contact2.latitude = 55.772319;
    contact2.longitude = 12.508964;

    var contact3 = new Contact();
    contact3.id = 0;
    contact3.name = "Francesc de Puig";
    contact3.surname = "Guixé";
    contact3.phone = "(+34) 657 865 435";
    contact3.email = "francescdepuig@gmail.com";
    contact3.latitude = 55.761173;
    contact3.longitude = 12.522551;

    this.contacts.push(contact1);
    this.contacts.push(contact2);
    this.contacts.push(contact3);

    this.user.contactsArray = this.contacts;
  }

  loadContacts(data){

  }

  startObservablDrawingMarkers() {
      var httpServ = this.mHTTPService;
      var usID = this.userID;
      var drawMarkersFunc = this.drawMarkers;
      var map = this.map;
      var markers = this.markers;
      setTimeout(function() {
        httpServ.sendRequestAtenders(usID).subscribe(data => {
          console.log(data);
          var coordinates = [];
          for (var i = 0; i < data.length; ++i) {
            if (data[i].latlong !== undefined) {
              var latlong = data[i].latlong.split(',');
              coordinates[coordinates.length] = {lat: latlong[0], lon: latlong[1]};
            }
          }
          console.log(coordinates);
          drawMarkersFunc(map, markers, coordinates);
        });
      }, 5000);
  }

  //TODO Poner con información correcta de servidor
  drawMarkers(map, markers, data) {
    console.log(markers);
    for (let marker of markers) {
      map.removeLayer(marker);
    }
    for (var i = 0; i < data.length; ++i) {
      markers.push(L.marker([data[i].lat, data[i].lon]).addTo(map));
    }
    console.log(markers);
  }

  openDialog() {
    let dialogRef = this.dialog.open(DialogResult);
    dialogRef.afterClosed().subscribe(result => {
      var result = result.split("#");

      console.log(result.length);
      if (result.length == 4) {
        console.log('dddd');
        var name = result[0];
        var surname = result[1];
        var email = result[2];
        var phone = result[3];

        //Enviar a backend para crear Contacto y refrescar pagina
        this.mHTTPService.addContact(null).subscribe(data =>
            "");
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
