import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EventTypeComponent } from '../list/event-type.component';
import { EventTypeDetailComponent } from '../detail/event-type-detail.component';
import { EventTypeUpdateComponent } from '../update/event-type-update.component';
import { EventTypeRoutingResolveService } from './event-type-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eventTypeRoute: Routes = [
  {
    path: '',
    component: EventTypeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EventTypeDetailComponent,
    resolve: {
      eventType: EventTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EventTypeUpdateComponent,
    resolve: {
      eventType: EventTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EventTypeUpdateComponent,
    resolve: {
      eventType: EventTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eventTypeRoute)],
  exports: [RouterModule],
})
export class EventTypeRoutingModule {}
