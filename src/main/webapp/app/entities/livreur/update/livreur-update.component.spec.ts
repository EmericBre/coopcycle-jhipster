import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LivreurService } from '../service/livreur.service';
import { ILivreur, Livreur } from '../livreur.model';

import { LivreurUpdateComponent } from './livreur-update.component';

describe('Livreur Management Update Component', () => {
  let comp: LivreurUpdateComponent;
  let fixture: ComponentFixture<LivreurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let livreurService: LivreurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LivreurUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(LivreurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LivreurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    livreurService = TestBed.inject(LivreurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const livreur: ILivreur = { id: 'CBA' };

      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(livreur));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livreur>>();
      const livreur = { id: 'ABC' };
      jest.spyOn(livreurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livreur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(livreurService.update).toHaveBeenCalledWith(livreur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livreur>>();
      const livreur = new Livreur();
      jest.spyOn(livreurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: livreur }));
      saveSubject.complete();

      // THEN
      expect(livreurService.create).toHaveBeenCalledWith(livreur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Livreur>>();
      const livreur = { id: 'ABC' };
      jest.spyOn(livreurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ livreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(livreurService.update).toHaveBeenCalledWith(livreur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
