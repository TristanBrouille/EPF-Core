import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BulletinComponent } from './bulletin.component';

describe('BulletinComponent', () => {
  let component: BulletinComponent;
  let fixture: ComponentFixture<BulletinComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BulletinComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BulletinComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
