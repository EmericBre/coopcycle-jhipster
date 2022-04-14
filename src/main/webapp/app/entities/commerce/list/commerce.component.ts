import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommerce } from '../commerce.model';
import { CommerceService } from '../service/commerce.service';
import { CommerceDeleteDialogComponent } from '../delete/commerce-delete-dialog.component';

@Component({
  selector: 'jhi-commerce',
  templateUrl: './commerce.component.html',
})
export class CommerceComponent implements OnInit {
  commerce?: ICommerce[];
  isLoading = false;

  constructor(protected commerceService: CommerceService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.commerceService.query().subscribe({
      next: (res: HttpResponse<ICommerce[]>) => {
        this.isLoading = false;
        this.commerce = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICommerce): string {
    return item.id!;
  }

  delete(commerce: ICommerce): void {
    const modalRef = this.modalService.open(CommerceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commerce = commerce;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
