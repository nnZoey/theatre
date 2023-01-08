import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISeat } from 'app/entities/seat/seat.model';
import { SeatService } from 'app/entities/seat/service/seat.service';

import { IEvent } from '../event.model';

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

  constructor(protected activatedRoute: ActivatedRoute, protected seatService: SeatService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ event }) => {
      this.event = event;

      const stageId = event.stage?.id;
      if (stageId !== undefined) {
        this.seatService.query({ 'stageId.equals': stageId }).subscribe({
          next: res => {
            this.seats = res.body ?? [];
            this.occupiedSeats = this.seats.filter(s => s.occupied);
            while (this.seats.length > 0) {
              const seat = this.seats[0];
              const alignedSeats = this.seats.filter(s => s.row === seat.row);
              this.alignedSeats.push(alignedSeats);
              this.seats = this.seats.filter(s => s.row !== seat.row);
            }
          },
        });
      }
    });

    // generate seats row is char, column is number
    for (let i = 0; i < 10; i++) {
      for (let j = 0; j < 10; j++) {
        const seat: ISeat = {
          id: i * 10 + j,
          row: String.fromCharCode(65 + i),
          col: j,
        };
        this.seats.push(seat);
      }
    }

    for (let i = 0; i < 10; i++) {
      const seat = this.seats[Math.floor(Math.random() * this.seats.length)];
      this.occupiedSeats.push(seat);
    }

    while (this.seats.length > 0) {
      const seat = this.seats[0];
      const alignedSeats = this.seats.filter(s => s.row === seat.row);
      this.alignedSeats.push(alignedSeats);
      this.seats = this.seats.filter(s => s.row !== seat.row);
    }
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

  pay(): void {
    this.paying = true;
  }

  cancelPay(): void {
    this.paying = false;
  }
}
