import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CommandeService } from '../service/commande.service';
import { ICommande, Commande } from '../commande.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';

import { CommandeUpdateComponent } from './commande-update.component';

describe('Commande Management Update Component', () => {
  let comp: CommandeUpdateComponent;
  let fixture: ComponentFixture<CommandeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let commandeService: CommandeService;
  let utilisateurService: UtilisateurService;
  let livreurService: LivreurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CommandeUpdateComponent],
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
      .overrideTemplate(CommandeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommandeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    commandeService = TestBed.inject(CommandeService);
    utilisateurService = TestBed.inject(UtilisateurService);
    livreurService = TestBed.inject(LivreurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Utilisateur query and add missing value', () => {
      const commande: ICommande = { id: 'CBA' };
      const utilisateur: IUtilisateur = { id: 'ec933b19-da2a-4167-85be-a5693b8f313c' };
      commande.utilisateur = utilisateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: '62597e37-e374-42a1-89b9-868650a72b0f' }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [utilisateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(utilisateurCollection, ...additionalUtilisateurs);
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Livreur query and add missing value', () => {
      const commande: ICommande = { id: 'CBA' };
      const livreur: ILivreur = { id: '205a00a0-62b6-406f-9b53-84d435295c02' };
      commande.livreur = livreur;

      const livreurCollection: ILivreur[] = [{ id: '1c4dccdd-7d0d-4ff9-8d91-94e009eca11d' }];
      jest.spyOn(livreurService, 'query').mockReturnValue(of(new HttpResponse({ body: livreurCollection })));
      const additionalLivreurs = [livreur];
      const expectedCollection: ILivreur[] = [...additionalLivreurs, ...livreurCollection];
      jest.spyOn(livreurService, 'addLivreurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(livreurService.query).toHaveBeenCalled();
      expect(livreurService.addLivreurToCollectionIfMissing).toHaveBeenCalledWith(livreurCollection, ...additionalLivreurs);
      expect(comp.livreursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const commande: ICommande = { id: 'CBA' };
      const utilisateur: IUtilisateur = { id: 'ccf86520-5944-4068-9f06-43ff8715eff4' };
      commande.utilisateur = utilisateur;
      const livreur: ILivreur = { id: 'e48f8ea7-ee61-47de-9d78-66361def5739' };
      commande.livreur = livreur;

      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(commande));
      expect(comp.utilisateursSharedCollection).toContain(utilisateur);
      expect(comp.livreursSharedCollection).toContain(livreur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commande>>();
      const commande = { id: 'ABC' };
      jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commande }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(commandeService.update).toHaveBeenCalledWith(commande);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commande>>();
      const commande = new Commande();
      jest.spyOn(commandeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: commande }));
      saveSubject.complete();

      // THEN
      expect(commandeService.create).toHaveBeenCalledWith(commande);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Commande>>();
      const commande = { id: 'ABC' };
      jest.spyOn(commandeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ commande });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(commandeService.update).toHaveBeenCalledWith(commande);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUtilisateurById', () => {
      it('Should return tracked Utilisateur primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackUtilisateurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackLivreurById', () => {
      it('Should return tracked Livreur primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackLivreurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
