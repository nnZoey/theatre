import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISeat } from '../seat.model';

@Component({
  selector: 'jhi-seat-detail',
  templateUrl: './seat-detail.component.html',
})
export class SeatDetailComponent implements OnInit {
  seat: ISeat | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seat }) => {
      this.seat = seat;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
