import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FicheEtudiant } from './fiche-etudiant';

describe('FicheEtudiant', () => {
  let component: FicheEtudiant;
  let fixture: ComponentFixture<FicheEtudiant>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FicheEtudiant]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FicheEtudiant);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
