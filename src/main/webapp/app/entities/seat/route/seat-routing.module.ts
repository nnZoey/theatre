import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SeatComponent } from '../list/seat.component';
import { SeatDetailComponent } from '../detail/seat-detail.component';
import { SeatUpdateComponent } from '../update/seat-update.component';
import { SeatRoutingResolveService } from './seat-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const seatRoute: Routes = [
  {
    path: '',
    component: SeatComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SeatDetailComponent,
    resolve: {
      seat: SeatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SeatUpdateComponent,
    resolve: {
      seat: SeatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SeatUpdateComponent,
    resolve: {
      seat: SeatRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(seatRoute)],
  exports: [RouterModule],
})
export class SeatRoutingModule {}
