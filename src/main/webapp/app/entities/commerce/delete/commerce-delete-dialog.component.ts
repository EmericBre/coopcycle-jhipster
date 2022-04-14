import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommerce } from '../commerce.model';
import { CommerceService } from '../service/commerce.service';

@Component({
  templateUrl: './commerce-delete-dialog.component.html',
})
export class CommerceDeleteDialogComponent {
  commerce?: ICommerce;

  constructor(protected commerceService: CommerceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.commerceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
