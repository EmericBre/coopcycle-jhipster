import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PaiementService } from '../service/paiement.service';

import { PaiementComponent } from './paiement.component';

describe('Paiement Management Component', () => {
  let comp: PaiementComponent;
  let fixture: ComponentFixture<PaiementComponent>;
  let service: PaiementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PaiementComponent],
    })
      .overrideTemplate(PaiementComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaiementComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PaiementService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.paiements?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
