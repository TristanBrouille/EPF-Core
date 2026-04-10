import { Component } from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {Header} from './decor/header/header';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {Sidebar} from './decor/sidebar/sidebar';
import {Footer} from './decor/footer/footer';
import {filter} from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, MatSidenavContainer, MatSidenav, Sidebar, MatSidenavContent, Footer],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected showLayout = true;
  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.showLayout = event.urlAfterRedirects !== '/login';
      });
  }
}
