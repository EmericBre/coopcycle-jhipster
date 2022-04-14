import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommerceDetailComponent } from './commerce-detail.component';

describe('Commerce Management Detail Component', () => {
  let comp: CommerceDetailComponent;
  let fixture: ComponentFixture<CommerceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommerceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commerce: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CommerceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommerceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commerce on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commerce).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
