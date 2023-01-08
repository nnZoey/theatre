import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { ITicket } from 'app/entities/ticket/ticket.model';

import { IOrder } from '../order.model';
import { OrderService } from '../service/order.service';

@Component({
  selector: 'jhi-order-detail',
  templateUrl: './order-detail.component.html',
})
export class OrderDetailComponent implements OnInit {
  order: IOrder | null = null;
  tickets: ITicket[] = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    private ticketService: TicketService,
    private accountService: AccountService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      this.order = order;

      this.ticketService.query({ 'orderId.equals': this.order?.id }).subscribe(res => {
        this.tickets = res.body ?? [];
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  getBadgeClass(order: IOrder): string {
    switch (order.status) {
      case OrderStatus.PAID:
        return 'badge text-bg-success';
      case OrderStatus.CANCELLED:
        return 'badge text-bg-danger';
      case OrderStatus.PENDING:
        return 'badge text-bg-warning';
      default:
        return '';
    }
  }

  getApproveButtonRendered(order: IOrder): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_SELLER']) && order.status !== OrderStatus.PAID;
  }

  getRejectButtonRendered(order: IOrder): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_SELLER']) && order.status !== OrderStatus.CANCELLED;
  }

  approve(order: IOrder): void {
    order.status = OrderStatus.PAID;
    this.orderService.partialUpdate(order).subscribe({
      next: () => {
        this.ngOnInit();
      },
    });
  }

  reject(order: IOrder): void {
    order.status = OrderStatus.CANCELLED;
    this.orderService.partialUpdate(order).subscribe({
      next: () => {
        this.ngOnInit();
      },
    });
  }
}
