import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommerceService } from '../service/commerce.service';
import { ICommerce, Commerce } from '../commerce.model';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { CommerceUpdateComponent } from './commerce-update.component';

describe('Commerce Management Update Component', () => {
  let comp: CommerceUpdateComponent;
  let fixture: ComponentFixture<CommerceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commerceService: CommerceService;
  let cooperativeService: CooperativeService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommerceUpdateComponent],
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
      .overrideTemplate(CommerceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommerceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commerceService = TestBed.inject(CommerceService);
    cooperativeService = TestBed.inject(CooperativeService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cooperative query and add missing value', () => {
      const commerce: ICommerce = { id: 'CBA' };
      const cooperatives: ICooperative[] = [{ id: '8a658287-9ee9-4ae6-a31d-80ff49571ba6' }];
      commerce.cooperatives = cooperatives;

      const cooperativeCollection: ICooperative[] = [{ id: '747e9614-1a70-4d72-8f9e-3b4d6bdcf32a' }];
      jest.spyOn(cooperativeService, 'query').mockReturnValue(of(new HttpResponse({ body: cooperativeCollection })));
      const additionalCooperatives = [...cooperatives];
      const expectedCollection: ICooperative[] = [...additionalCooperatives, ...cooperativeCollection];
      jest.spyOn(cooperativeService, 'addCooperativeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commerce });
      comp.ngOnInit();

      expect(cooperativeService.query).toHaveBeenCalled();
      expect(cooperativeService.addCooperativeToCollectionIfMissing).toHaveBeenCalledWith(cooperativeCollection, ...additionalCooperatives);
      expect(comp.cooperativesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Utilisateur query and add missing value', () => {
      const commerce: ICommerce = { id: 'CBA' };
      const utilisateur: IUtilisateur = { id: '51fcf5f6-0892-4501-97e0-8c84ac81fee6' };
      commerce.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 'f2965c59-848e-4436-b1ec-58c9ca858c19' }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commerce });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(utilisateurCollection, ...additionalUtilisateurs);
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commerce: ICommerce = { id: 'CBA' };
      const cooperatives: ICooperative = { id: '57d14fdc-4fca-4f38-9c03-16e1a9d15627' };
      commerce.cooperatives = [cooperatives];
      const utilisateur: IUtilisateur = { id: 'e56250d7-f1db-462f-b022-dd02967f0da3' };
      commerce.utilisateur = utilisateur;

      activatedRoute.data = of({ commerce });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commerce));
      expect(comp.cooperativesSharedCollection).toContain(cooperatives);
      expect(comp.utilisateursSharedCollection).toContain(utilisateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commerce>>();
      const commerce = { id: 'ABC' };
      jest.spyOn(commerceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commerce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commerce }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commerceService.update).toHaveBeenCalledWith(commerce);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commerce>>();
      const commerce = new Commerce();
      jest.spyOn(commerceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commerce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commerce }));
      saveSubject.complete();

      // THEN
      expect(commerceService.create).toHaveBeenCalledWith(commerce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commerce>>();
      const commerce = { id: 'ABC' };
      jest.spyOn(commerceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commerce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commerceService.update).toHaveBeenCalledWith(commerce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCooperativeById', () => {
      it('Should return tracked Cooperative primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackCooperativeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackUtilisateurById', () => {
      it('Should return tracked Utilisateur primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackUtilisateurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCooperative', () => {
      it('Should return option if no Cooperative is selected', () => {
        const option = { id: 'ABC' };
        const result = comp.getSelectedCooperative(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Cooperative for according option', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'ABC' };
        const selected2 = { id: 'CBA' };
        const result = comp.getSelectedCooperative(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Cooperative is not selected', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'CBA' };
        const result = comp.getSelectedCooperative(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
