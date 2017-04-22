import { Component } from '@angular/core';
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
  prueba = "SIN VALOR";

  constructor(private mHTTPService: HTTPService) {}

  clicked(message: string){
    this.mHTTPService.sendUpdateRequest(message).subscribe(data => this.prueba = data);
  }

  ngOnInit(): void {
    var map = L.map('map')
      .setView([51.505, -0.09], 13);

      L.tileLayer('http://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a> &copy; <a href="http://cartodb.com/attributions">CartoDB</a>',
      subdomains: 'abcd',
      maxZoom: 19
    }).addTo(map);

    var contact1 = new Contact();
    contact1.id = 0;
    contact1.name = "Marc";
    contact1.surname = "Vila";
    contact1.phone = "(+34) 689 754 378";
    contact1.email = "marcvilagomez@gmail.com";

    var contact2 = new Contact();
    contact2.id = 1;
    contact2.name = "Ester";
    contact2.surname = "Lorente";
    contact2.phone = "(+34) 657 654 356";
    contact2.email = "esterlorente@gmail.com";

    var contact3 = new Contact();
    contact3.id = 0;
    contact3.name = "Francesc de Puig";
    contact3.surname = "Guixé";
    contact3.phone = "(+34) 657 865 435";
    contact3.email = "francescdepuig@gmail.com";

    this.contacts.push(contact1);
    this.contacts.push(contact2);
    this.contacts.push(contact3);

    this.user.contactsArray = this.contacts;
  }
}
