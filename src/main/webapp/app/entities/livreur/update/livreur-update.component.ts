import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILivreur, Livreur } from '../livreur.model';
import { LivreurService } from '../service/livreur.service';

@Component({
  selector: 'jhi-livreur-update',
  templateUrl: './livreur-update.component.html',
})
export class LivreurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    firstname: [null, [Validators.required, Validators.maxLength(30), Validators.pattern('^[A-Z][a-z]+$')]],
    lastname: [null, [Validators.required, Validators.maxLength(30), Validators.pattern('^[A-Z][a-z]+$')]],
    phone: [null, [Validators.required]],
  });

  constructor(protected livreurService: LivreurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ livreur }) => {
      this.updateForm(livreur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const livreur = this.createFromForm();
    if (livreur.id !== undefined) {
      this.subscribeToSaveResponse(this.livreurService.update(livreur));
    } else {
      this.subscribeToSaveResponse(this.livreurService.create(livreur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILivreur>>): void {
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

  protected updateForm(livreur: ILivreur): void {
    this.editForm.patchValue({
      id: livreur.id,
      firstname: livreur.firstname,
      lastname: livreur.lastname,
      phone: livreur.phone,
    });
  }

  protected createFromForm(): ILivreur {
    return {
      ...new Livreur(),
      id: this.editForm.get(['id'])!.value,
      firstname: this.editForm.get(['firstname'])!.value,
      lastname: this.editForm.get(['lastname'])!.value,
      phone: this.editForm.get(['phone'])!.value,
    };
  }
}
