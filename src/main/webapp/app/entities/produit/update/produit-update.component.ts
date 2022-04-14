import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduit, Produit } from '../produit.model';
import { ProduitService } from '../service/produit.service';
import { ICommande } from 'app/entities/commande/commande.model';
import { CommandeService } from 'app/entities/commande/service/commande.service';
import { ICommerce } from 'app/entities/commerce/commerce.model';
import { CommerceService } from 'app/entities/commerce/service/commerce.service';

@Component({
  selector: 'jhi-produit-update',
  templateUrl: './produit-update.component.html',
})
export class ProduitUpdateComponent implements OnInit {
  isSaving = false;

  commandesSharedCollection: ICommande[] = [];
  commerceSharedCollection: ICommerce[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    price: [null, [Validators.required, Validators.min(0)]],
    type: [null, [Validators.required]],
    description: [],
    commandes: [],
    commerce: [],
  });

  constructor(
    protected produitService: ProduitService,
    protected commandeService: CommandeService,
    protected commerceService: CommerceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ produit }) => {
      this.updateForm(produit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const produit = this.createFromForm();
    if (produit.id !== undefined) {
      this.subscribeToSaveResponse(this.produitService.update(produit));
    } else {
      this.subscribeToSaveResponse(this.produitService.create(produit));
    }
  }

  trackCommandeById(_index: number, item: ICommande): string {
    return item.id!;
  }

  trackCommerceById(_index: number, item: ICommerce): string {
    return item.id!;
  }

  getSelectedCommande(option: ICommande, selectedVals?: ICommande[]): ICommande {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(produit: IProduit): void {
    this.editForm.patchValue({
      id: produit.id,
      price: produit.price,
      type: produit.type,
      description: produit.description,
      commandes: produit.commandes,
      commerce: produit.commerce,
    });

    this.commandesSharedCollection = this.commandeService.addCommandeToCollectionIfMissing(
      this.commandesSharedCollection,
      ...(produit.commandes ?? [])
    );
    this.commerceSharedCollection = this.commerceService.addCommerceToCollectionIfMissing(this.commerceSharedCollection, produit.commerce);
  }

  protected loadRelationshipsOptions(): void {
    this.commandeService
      .query()
      .pipe(map((res: HttpResponse<ICommande[]>) => res.body ?? []))
      .pipe(
        map((commandes: ICommande[]) =>
          this.commandeService.addCommandeToCollectionIfMissing(commandes, ...(this.editForm.get('commandes')!.value ?? []))
        )
      )
      .subscribe((commandes: ICommande[]) => (this.commandesSharedCollection = commandes));

    this.commerceService
      .query()
      .pipe(map((res: HttpResponse<ICommerce[]>) => res.body ?? []))
      .pipe(
        map((commerce: ICommerce[]) =>
          this.commerceService.addCommerceToCollectionIfMissing(commerce, this.editForm.get('commerce')!.value)
        )
      )
      .subscribe((commerce: ICommerce[]) => (this.commerceSharedCollection = commerce));
  }

  protected createFromForm(): IProduit {
    return {
      ...new Produit(),
      id: this.editForm.get(['id'])!.value,
      price: this.editForm.get(['price'])!.value,
      type: this.editForm.get(['type'])!.value,
      description: this.editForm.get(['description'])!.value,
      commandes: this.editForm.get(['commandes'])!.value,
      commerce: this.editForm.get(['commerce'])!.value,
    };
  }
}
