import { TestBed } from '@angular/core/testing';

import { Boletos } from './boletos';

describe('Boletos', () => {
  let service: Boletos;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Boletos);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
