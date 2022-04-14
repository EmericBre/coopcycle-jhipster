import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICooperative } from '../cooperative.model';
import { CooperativeService } from '../service/cooperative.service';

@Component({
  templateUrl: './cooperative-delete-dialog.component.html',
})
export class CooperativeDeleteDialogComponent {
  cooperative?: ICooperative;

  constructor(protected cooperativeService: CooperativeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.cooperativeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
