import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaiement } from '../paiement.model';
import { PaiementService } from '../service/paiement.service';
import { PaiementDeleteDialogComponent } from '../delete/paiement-delete-dialog.component';

@Component({
  selector: 'jhi-paiement',
  templateUrl: './paiement.component.html',
})
export class PaiementComponent implements OnInit {
  paiements?: IPaiement[];
  isLoading = false;

  constructor(protected paiementService: PaiementService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.paiementService.query().subscribe({
      next: (res: HttpResponse<IPaiement[]>) => {
        this.isLoading = false;
        this.paiements = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPaiement): number {
    return item.id!;
  }

  delete(paiement: IPaiement): void {
    const modalRef = this.modalService.open(PaiementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paiement = paiement;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
