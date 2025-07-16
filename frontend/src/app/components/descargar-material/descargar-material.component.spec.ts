import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DescargarMaterialComponent } from './descargar-material.component';

describe('DescargarMaterialComponent', () => {
  let component: DescargarMaterialComponent;
  let fixture: ComponentFixture<DescargarMaterialComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DescargarMaterialComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DescargarMaterialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
