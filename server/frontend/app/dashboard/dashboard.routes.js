"use strict";
var home_component_1 = require('./home/home.component');
var user_component_1 = require('./user/user.component');
var table_component_1 = require('./table/table.component');
var maps_component_1 = require('./maps/maps.component');
exports.MODULE_ROUTES = [
    { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: home_component_1.HomeComponent },
    { path: 'user', component: user_component_1.UserComponent },
    { path: 'table', component: table_component_1.TableComponent },
    { path: 'maps', component: maps_component_1.MapsComponent }
];
exports.MODULE_COMPONENTS = [
    home_component_1.HomeComponent,
    user_component_1.UserComponent,
    table_component_1.TableComponent,
    maps_component_1.MapsComponent
];
//# sourceMappingURL=dashboard.routes.js.map