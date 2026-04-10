import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Login} from './login/login';
import { BulletinComponent } from './bulletin.component/bulletin.component';

import { Component } from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {Header} from './decor/header/header';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {Sidebar} from './decor/sidebar/sidebar';
import {Footer} from './decor/footer/footer';
import {filter} from 'rxjs';
import { ArchiveModalService } from './app.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Header, MatSidenavContainer, MatSidenav, Sidebar, MatSidenavContent, Footer, CommonModule,BulletinComponent,BulletinComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected showLayout = true;
  protected showSidebar = true;


  constructor(private router: Router, protected archiveModal: ArchiveModalService) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.showLayout =
          event.urlAfterRedirects !== '/login' &&
          event.urlAfterRedirects !== '/register-candidat';

        this.showSidebar =
          event.urlAfterRedirects !== '/candidat' &&
          event.urlAfterRedirects !== '/formulaire-inscription';
      });
  }

  documentTypeLabel(type: string | undefined): string {
    if (!type) return '';
    return ({
      CERTIFICATE_SCOLAR: 'Certificat de scolarité',
      INFOS_STUDENT: 'Informations personnelles',
    } as Record<string, string>)[type] ?? type;
  }

}
