import 'hammerjs';

import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { MaterialModule } from '@angular/material';

import { HTTPService } from './services/HTTPService';

import { AppComponent, DialogResult } from './app.component';

@NgModule({
  declarations: [
    AppComponent, DialogResult
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    MaterialModule.forRoot()
  ],
  providers: [HTTPService],
  bootstrap: [AppComponent],
  entryComponents: [DialogResult]
})
export class AppModule { }
