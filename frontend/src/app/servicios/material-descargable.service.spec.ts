import { TestBed } from '@angular/core/testing';

import { MaterialDescargableService } from './material-descargable.service';

describe('MaterialDescargableService', () => {
  let service: MaterialDescargableService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MaterialDescargableService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
