import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StageComponent } from './list/stage.component';
import { StageDetailComponent } from './detail/stage-detail.component';
import { StageUpdateComponent } from './update/stage-update.component';
import { StageDeleteDialogComponent } from './delete/stage-delete-dialog.component';
import { StageRoutingModule } from './route/stage-routing.module';

@NgModule({
  imports: [SharedModule, StageRoutingModule],
  declarations: [StageComponent, StageDetailComponent, StageUpdateComponent, StageDeleteDialogComponent],
})
export class StageModule {}
