import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { NewOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { ISeat } from 'app/entities/seat/seat.model';
import { SeatService } from 'app/entities/seat/service/seat.service';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import dayjs from 'dayjs/esm';

import { IEvent } from '../event.model';

const basePrice = 220000;
const classPrice: { [key: string]: number } = {
  A: 1.5,
  B: 1.2,
  C: 1,
};

@Component({
  selector: 'jhi-event-detail',
  templateUrl: './event-detail.component.html',
})
export class EventDetailComponent implements OnInit {
  event: IEvent | null = null;
  seats: ISeat[] = [];
  alignedSeats: ISeat[][] = [];
  selectedSeats: ISeat[] = [];
  occupiedSeats: ISeat[] = [];
  paying = false;
  transactionCode = '';
  confirmingPay = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected seatService: SeatService,
    protected ticketService: TicketService,
    protected orderService: OrderService,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      this.event = event;
      this.alignedSeats = [];
      this.selectedSeats = [];
      this.occupiedSeats = [];
      this.transactionCode = '';

      const stageId = event.stage?.id;
      if (stageId !== undefined) {
        this.seatService.query({ 'stageId.equals': stageId, size: 200 }).subscribe({
          next: res => {
            this.seats = res.body ?? [];
            while (this.seats.length > 0) {
              const seat = this.seats[0];
              const alignedSeats = this.seats.filter(s => s.row === seat.row);
              this.alignedSeats.push(alignedSeats);
              this.seats = this.seats.filter(s => s.row !== seat.row);
            }
          },
        });
      }

      // get all orders then get all tickets then map to seats
      this.orderService.query({ 'eventId.equals': event.id, size: 200 }).subscribe({
        next: res => {
          const orders = res.body ?? [];
          orders.forEach(order => {
            this.ticketService.query({ 'orderId.in': order.id, size: 200 }).subscribe({
              next: ticketRes => {
                const tickets = ticketRes.body ?? [];
                tickets.forEach(ticket => {
                  this.occupiedSeats.push(ticket.seat!);
                });
              },
            });
          });
        },
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  selectSeat(seat: ISeat): void {
    if (this.selectedSeats.find(s => s.id === seat.id) !== undefined) {
      this.selectedSeats = this.selectedSeats.filter(s => s.id !== seat.id);
    } else {
      this.selectedSeats.push(seat);
    }
  }

  isSelected(seat: ISeat): boolean {
    return this.selectedSeats.find(s => s.id === seat.id) !== undefined;
  }

  isOccupied(seat: ISeat): boolean {
    return this.occupiedSeats.find(s => s.id === seat.id) !== undefined;
  }

  getSeatClass(seat: ISeat): string {
    if (this.isOccupied(seat)) {
      return 'btn-outline-danger';
    }
    if (this.isSelected(seat)) {
      return 'btn-success';
    }
    return 'btn-outline-primary';
  }

  getSelectedSeats(): string {
    return this.selectedSeats
      .map(seat => String(seat.row) + String(seat.col))
      .sort()
      .join(', ');
  }

  getTotalPrice(): number {
    return this.selectedSeats.reduce((total, seat) => total + basePrice * classPrice[seat.seatClass!], 0);
  }

  getFormattedPrice(): string {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(this.getTotalPrice());
  }
  // price base on class of seat

  pay(): void {
    this.paying = true;
  }

  cancelPay(): void {
    this.paying = false;
  }

  confirmPay(): void {
    this.confirmingPay = true;
    this.accountService.getCurrentAppUser().subscribe(appUser => {
      const order: NewOrder = {
        id: null,
        status: OrderStatus.PENDING,
        transactionCode: this.transactionCode,
        isPaid: false,
        issuedDate: dayjs(),
        appUser: { id: appUser.id },
        event: { id: this.event?.id ?? 0 },
      };

      this.orderService.create(order).subscribe({
        next: res => {
          const orderId = res.body?.id;
          if (orderId !== undefined) {
            this.selectedSeats.forEach(seat =>
              this.ticketService
                .create({
                  seat: { id: seat.id },
                  order: { id: orderId },
                  price: basePrice * classPrice[seat.seatClass!],
                  id: null,
                })
                .subscribe()
            );
          }
          this.confirmingPay = false;
          this.paying = false;
          this.ngOnInit();
        },
      });
    });
  }
}
