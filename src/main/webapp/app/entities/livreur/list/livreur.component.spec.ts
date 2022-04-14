import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LivreurService } from '../service/livreur.service';

import { LivreurComponent } from './livreur.component';

describe('Livreur Management Component', () => {
  let comp: LivreurComponent;
  let fixture: ComponentFixture<LivreurComponent>;
  let service: LivreurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LivreurComponent],
    })
      .overrideTemplate(LivreurComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivreurComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LivreurService);

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
    expect(comp.livreurs?.[0]).toEqual(expect.objectContaining({ id: 'ABC' }));
  });
});
