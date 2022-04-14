import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommerce, Commerce } from '../commerce.model';
import { CommerceService } from '../service/commerce.service';

@Injectable({ providedIn: 'root' })
export class CommerceRoutingResolveService implements Resolve<ICommerce> {
  constructor(protected service: CommerceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICommerce> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((commerce: HttpResponse<Commerce>) => {
          if (commerce.body) {
            return of(commerce.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Commerce());
  }
}
