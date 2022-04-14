import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommerce, Commerce } from '../commerce.model';
import { CommerceService } from '../service/commerce.service';
import { ICooperative } from 'app/entities/cooperative/cooperative.model';
import { CooperativeService } from 'app/entities/cooperative/service/cooperative.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

@Component({
  selector: 'jhi-commerce-update',
  templateUrl: './commerce-update.component.html',
})
export class CommerceUpdateComponent implements OnInit {
  isSaving = false;

  cooperativesSharedCollection: ICooperative[] = [];
  utilisateursSharedCollection: IUtilisateur[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    name: [null, [Validators.required]],
    adress: [null, [Validators.required]],
    cooperatives: [],
    utilisateur: [],
  });

  constructor(
    protected commerceService: CommerceService,
    protected cooperativeService: CooperativeService,
    protected utilisateurService: UtilisateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commerce }) => {
      this.updateForm(commerce);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commerce = this.createFromForm();
    if (commerce.id !== undefined) {
      this.subscribeToSaveResponse(this.commerceService.update(commerce));
    } else {
      this.subscribeToSaveResponse(this.commerceService.create(commerce));
    }
  }

  trackCooperativeById(_index: number, item: ICooperative): string {
    return item.id!;
  }

  trackUtilisateurById(_index: number, item: IUtilisateur): string {
    return item.id!;
  }

  getSelectedCooperative(option: ICooperative, selectedVals?: ICooperative[]): ICooperative {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommerce>>): void {
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

  protected updateForm(commerce: ICommerce): void {
    this.editForm.patchValue({
      id: commerce.id,
      name: commerce.name,
      adress: commerce.adress,
      cooperatives: commerce.cooperatives,
      utilisateur: commerce.utilisateur,
    });

    this.cooperativesSharedCollection = this.cooperativeService.addCooperativeToCollectionIfMissing(
      this.cooperativesSharedCollection,
      ...(commerce.cooperatives ?? [])
    );
    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursSharedCollection,
      commerce.utilisateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cooperativeService
      .query()
      .pipe(map((res: HttpResponse<ICooperative[]>) => res.body ?? []))
      .pipe(
        map((cooperatives: ICooperative[]) =>
          this.cooperativeService.addCooperativeToCollectionIfMissing(cooperatives, ...(this.editForm.get('cooperatives')!.value ?? []))
        )
      )
      .subscribe((cooperatives: ICooperative[]) => (this.cooperativesSharedCollection = cooperatives));

    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.editForm.get('utilisateur')!.value)
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }

  protected createFromForm(): ICommerce {
    return {
      ...new Commerce(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      cooperatives: this.editForm.get(['cooperatives'])!.value,
      utilisateur: this.editForm.get(['utilisateur'])!.value,
    };
  }
}
