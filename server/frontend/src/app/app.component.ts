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

  constructor(private mHTTPService: HTTPService, public dialog: MdDialog) {}

  clicked(message: string){
    var userID = "58fbc23ba926e6737f157ec6";
    this.mHTTPService.sendUpdateRequestSMS(userID, message).subscribe(data =>
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
      Observable.interval(5000).subscribe(x => {
        this.mHTTPService.sendUpdateRequestLocation().subscribe(data =>
            this.drawMarkers(data));
      });
  }

  //TODO Poner con información correcta de servidor
  drawMarkers(data) {
    this.prueba = data;
    for (let marker of this.markers) {
      this.map.removeLayer(marker);
    }
    this.markers.push(L.marker([55.768028, 12.503128]).addTo(this.map));
    this.markers.push(L.marker([55.772319, 12.508964]).addTo(this.map));
    this.markers.push(L.marker([55.761173, 12.522551]).addTo(this.map));
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
