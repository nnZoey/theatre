import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StageComponent } from '../list/stage.component';
import { StageDetailComponent } from '../detail/stage-detail.component';
import { StageUpdateComponent } from '../update/stage-update.component';
import { StageRoutingResolveService } from './stage-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const stageRoute: Routes = [
  {
    path: '',
    component: StageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StageDetailComponent,
    resolve: {
      stage: StageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StageUpdateComponent,
    resolve: {
      stage: StageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StageUpdateComponent,
    resolve: {
      stage: StageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(stageRoute)],
  exports: [RouterModule],
})
export class StageRoutingModule {}
