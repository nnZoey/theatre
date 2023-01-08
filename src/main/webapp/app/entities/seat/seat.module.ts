import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SeatComponent } from './list/seat.component';
import { SeatDetailComponent } from './detail/seat-detail.component';
import { SeatUpdateComponent } from './update/seat-update.component';
import { SeatDeleteDialogComponent } from './delete/seat-delete-dialog.component';
import { SeatRoutingModule } from './route/seat-routing.module';

@NgModule({
  imports: [SharedModule, SeatRoutingModule],
  declarations: [SeatComponent, SeatDetailComponent, SeatUpdateComponent, SeatDeleteDialogComponent],
})
export class SeatModule {}
