import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivreur } from '../livreur.model';
import { LivreurService } from '../service/livreur.service';

@Component({
  templateUrl: './livreur-delete-dialog.component.html',
})
export class LivreurDeleteDialogComponent {
  livreur?: ILivreur;

  constructor(protected livreurService: LivreurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.livreurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
