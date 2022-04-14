import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICommerce } from '../commerce.model';

@Component({
  selector: 'jhi-commerce-detail',
  templateUrl: './commerce-detail.component.html',
})
export class CommerceDetailComponent implements OnInit {
  commerce: ICommerce | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commerce }) => {
      this.commerce = commerce;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
