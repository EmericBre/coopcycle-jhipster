import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CooperativeService } from '../service/cooperative.service';

import { CooperativeComponent } from './cooperative.component';

describe('Cooperative Management Component', () => {
  let comp: CooperativeComponent;
  let fixture: ComponentFixture<CooperativeComponent>;
  let service: CooperativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CooperativeComponent],
    })
      .overrideTemplate(CooperativeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CooperativeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CooperativeService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 'ABC' }],
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
    expect(comp.cooperatives?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
