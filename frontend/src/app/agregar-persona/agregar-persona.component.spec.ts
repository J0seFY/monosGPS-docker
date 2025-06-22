import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgregarPersonaComponent } from './agregar-persona.component';

describe('AgregarPersonaComponent', () => {
  let component: AgregarPersonaComponent;
  let fixture: ComponentFixture<AgregarPersonaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AgregarPersonaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AgregarPersonaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
