import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILivreur } from '../livreur.model';
import { LivreurService } from '../service/livreur.service';
import { LivreurDeleteDialogComponent } from '../delete/livreur-delete-dialog.component';

@Component({
  selector: 'jhi-livreur',
  templateUrl: './livreur.component.html',
})
export class LivreurComponent implements OnInit {
  livreurs?: ILivreur[];
  isLoading = false;

  constructor(protected livreurService: LivreurService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.livreurService.query().subscribe({
      next: (res: HttpResponse<ILivreur[]>) => {
        this.isLoading = false;
        this.livreurs = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILivreur): string {
    return item.id!;
  }

  delete(livreur: ILivreur): void {
    const modalRef = this.modalService.open(LivreurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.livreur = livreur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
