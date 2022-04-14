import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;

  utilisateursSharedCollection: IUtilisateur[] = [];
  livreursSharedCollection: ILivreur[] = [];

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    utilisateur: [],
    livreur: [],
  });

  constructor(
    protected commandeService: CommandeService,
    protected utilisateurService: UtilisateurService,
    protected livreurService: LivreurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.updateForm(commande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  trackUtilisateurById(_index: number, item: IUtilisateur): string {
    return item.id!;
  }

  trackLivreurById(_index: number, item: ILivreur): string {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
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

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      utilisateur: commande.utilisateur,
      livreur: commande.livreur,
    });

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursSharedCollection,
      commande.utilisateur
    );
    this.livreursSharedCollection = this.livreurService.addLivreurToCollectionIfMissing(this.livreursSharedCollection, commande.livreur);
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.editForm.get('utilisateur')!.value)
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));

    this.livreurService
      .query()
      .pipe(map((res: HttpResponse<ILivreur[]>) => res.body ?? []))
      .pipe(
        map((livreurs: ILivreur[]) => this.livreurService.addLivreurToCollectionIfMissing(livreurs, this.editForm.get('livreur')!.value))
      )
      .subscribe((livreurs: ILivreur[]) => (this.livreursSharedCollection = livreurs));
  }

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      utilisateur: this.editForm.get(['utilisateur'])!.value,
      livreur: this.editForm.get(['livreur'])!.value,
    };
  }
}
