import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProduitService } from '../service/produit.service';
import { IProduit, Produit } from '../produit.model';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { ICommerce } from 'app/entities/commerce/commerce.model';
import { CommerceService } from 'app/entities/commerce/service/commerce.service';

import { ProduitUpdateComponent } from './produit-update.component';

describe('Produit Management Update Component', () => {
  let comp: ProduitUpdateComponent;
  let fixture: ComponentFixture<ProduitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let produitService: ProduitService;
  let commandeService: CommandeService;
  let commerceService: CommerceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProduitUpdateComponent],
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
      .overrideTemplate(ProduitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProduitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    produitService = TestBed.inject(ProduitService);
    commandeService = TestBed.inject(CommandeService);
    commerceService = TestBed.inject(CommerceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Commande query and add missing value', () => {
      const produit: IProduit = { id: 'CBA' };
      const commandes: ICommande[] = [{ id: '327216ff-8075-4ca3-b09d-d2c808b5aef8' }];
      produit.commandes = commandes;

      const commandeCollection: ICommande[] = [{ id: 'a85c3fe7-9803-4ab4-8c34-58202648dbdb' }];
      jest.spyOn(commandeService, 'query').mockReturnValue(of(new HttpResponse({ body: commandeCollection })));
      const additionalCommandes = [...commandes];
      const expectedCollection: ICommande[] = [...additionalCommandes, ...commandeCollection];
      jest.spyOn(commandeService, 'addCommandeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(commandeService.query).toHaveBeenCalled();
      expect(commandeService.addCommandeToCollectionIfMissing).toHaveBeenCalledWith(commandeCollection, ...additionalCommandes);
      expect(comp.commandesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Commerce query and add missing value', () => {
      const produit: IProduit = { id: 'CBA' };
      const commerce: ICommerce = { id: '3a94fa62-3817-4e84-a7db-6ee1348b5b8b' };
      produit.commerce = commerce;

      const commerceCollection: ICommerce[] = [{ id: '50b0e676-65e2-47fc-ae8f-e2113d6c1d7b' }];
      jest.spyOn(commerceService, 'query').mockReturnValue(of(new HttpResponse({ body: commerceCollection })));
      const additionalCommerce = [commerce];
      const expectedCollection: ICommerce[] = [...additionalCommerce, ...commerceCollection];
      jest.spyOn(commerceService, 'addCommerceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(commerceService.query).toHaveBeenCalled();
      expect(commerceService.addCommerceToCollectionIfMissing).toHaveBeenCalledWith(commerceCollection, ...additionalCommerce);
      expect(comp.commerceSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const produit: IProduit = { id: 'CBA' };
      const commandes: ICommande = { id: '2dbc0aa0-591a-40c3-b399-bf2ba9fa7132' };
      produit.commandes = [commandes];
      const commerce: ICommerce = { id: '91faa28a-c650-47e7-9122-926d1fac2a7d' };
      produit.commerce = commerce;

      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(produit));
      expect(comp.commandesSharedCollection).toContain(commandes);
      expect(comp.commerceSharedCollection).toContain(commerce);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produit>>();
      const produit = { id: 'ABC' };
      jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produit }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(produitService.update).toHaveBeenCalledWith(produit);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produit>>();
      const produit = new Produit();
      jest.spyOn(produitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: produit }));
      saveSubject.complete();

      // THEN
      expect(produitService.create).toHaveBeenCalledWith(produit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Produit>>();
      const produit = { id: 'ABC' };
      jest.spyOn(produitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ produit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(produitService.update).toHaveBeenCalledWith(produit);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCommandeById', () => {
      it('Should return tracked Commande primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackCommandeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCommerceById', () => {
      it('Should return tracked Commerce primary key', () => {
        const entity = { id: 'ABC' };
        const trackResult = comp.trackCommerceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedCommande', () => {
      it('Should return option if no Commande is selected', () => {
        const option = { id: 'ABC' };
        const result = comp.getSelectedCommande(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Commande for according option', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'ABC' };
        const selected2 = { id: 'CBA' };
        const result = comp.getSelectedCommande(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Commande is not selected', () => {
        const option = { id: 'ABC' };
        const selected = { id: 'CBA' };
        const result = comp.getSelectedCommande(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
