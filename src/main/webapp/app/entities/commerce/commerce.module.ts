import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CommerceComponent } from './list/commerce.component';
import { CommerceDetailComponent } from './detail/commerce-detail.component';
import { CommerceUpdateComponent } from './update/commerce-update.component';
import { CommerceDeleteDialogComponent } from './delete/commerce-delete-dialog.component';
import { CommerceRoutingModule } from './route/commerce-routing.module';

@NgModule({
  imports: [SharedModule, CommerceRoutingModule],
  declarations: [CommerceComponent, CommerceDetailComponent, CommerceUpdateComponent, CommerceDeleteDialogComponent],
  entryComponents: [CommerceDeleteDialogComponent],
})
export class CommerceModule {}
