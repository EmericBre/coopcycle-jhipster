import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CommerceComponent } from '../list/commerce.component';
import { CommerceDetailComponent } from '../detail/commerce-detail.component';
import { CommerceUpdateComponent } from '../update/commerce-update.component';
import { CommerceRoutingResolveService } from './commerce-routing-resolve.service';

const commerceRoute: Routes = [
  {
    path: '',
    component: CommerceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CommerceDetailComponent,
    resolve: {
      commerce: CommerceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CommerceUpdateComponent,
    resolve: {
      commerce: CommerceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CommerceUpdateComponent,
    resolve: {
      commerce: CommerceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(commerceRoute)],
  exports: [RouterModule],
})
export class CommerceRoutingModule {}
