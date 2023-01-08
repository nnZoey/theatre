import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'app-user',
        data: { pageTitle: 'AppUsers' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'event-type',
        data: { pageTitle: 'EventTypes' },
        loadChildren: () => import('./event-type/event-type.module').then(m => m.EventTypeModule),
      },
      {
        path: 'event',
        data: { pageTitle: 'Events' },
        loadChildren: () => import('./event/event.module').then(m => m.EventModule),
      },
      {
        path: 'seat',
        data: { pageTitle: 'Seats' },
        loadChildren: () => import('./seat/seat.module').then(m => m.SeatModule),
      },
      {
        path: 'stage',
        data: { pageTitle: 'Stages' },
        loadChildren: () => import('./stage/stage.module').then(m => m.StageModule),
      },
      {
        path: 'ticket',
        data: { pageTitle: 'Tickets' },
        loadChildren: () => import('./ticket/ticket.module').then(m => m.TicketModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'Orders' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
