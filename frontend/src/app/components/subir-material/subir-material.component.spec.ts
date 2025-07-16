import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubirMaterialComponent } from './subir-material.component';

describe('SubirMaterialComponent', () => {
  let component: SubirMaterialComponent;
  let fixture: ComponentFixture<SubirMaterialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SubirMaterialComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubirMaterialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
