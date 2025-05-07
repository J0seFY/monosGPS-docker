import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuscarPersonasComponent } from './buscar-personas.component';

describe('BuscarPersonasComponent', () => {
  let component: BuscarPersonasComponent;
  let fixture: ComponentFixture<BuscarPersonasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [BuscarPersonasComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BuscarPersonasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
