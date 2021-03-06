import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CommandeDetailComponent } from './commande-detail.component';

describe('Commande Management Detail Component', () => {
  let comp: CommandeDetailComponent;
  let fixture: ComponentFixture<CommandeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CommandeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ commande: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(CommandeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CommandeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load commande on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.commande).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
