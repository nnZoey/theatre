import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SeatFormService, SeatFormGroup } from './seat-form.service';
import { ISeat } from '../seat.model';
import { SeatService } from '../service/seat.service';
import { IStage } from 'app/entities/stage/stage.model';
import { StageService } from 'app/entities/stage/service/stage.service';

@Component({
  selector: 'jhi-seat-update',
  templateUrl: './seat-update.component.html',
})
export class SeatUpdateComponent implements OnInit {
  isSaving = false;
  seat: ISeat | null = null;

  stagesSharedCollection: IStage[] = [];

  editForm: SeatFormGroup = this.seatFormService.createSeatFormGroup();

  constructor(
    protected seatService: SeatService,
    protected seatFormService: SeatFormService,
    protected stageService: StageService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareStage = (o1: IStage | null, o2: IStage | null): boolean => this.stageService.compareStage(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ seat }) => {
      this.seat = seat;
      if (seat) {
        this.updateForm(seat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const seat = this.seatFormService.getSeat(this.editForm);
    if (seat.id !== null) {
      this.subscribeToSaveResponse(this.seatService.update(seat));
    } else {
      this.subscribeToSaveResponse(this.seatService.create(seat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISeat>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(seat: ISeat): void {
    this.seat = seat;
    this.seatFormService.resetForm(this.editForm, seat);

    this.stagesSharedCollection = this.stageService.addStageToCollectionIfMissing<IStage>(this.stagesSharedCollection, seat.stage);
  }

  protected loadRelationshipsOptions(): void {
    this.stageService
      .query()
      .pipe(map((res: HttpResponse<IStage[]>) => res.body ?? []))
      .pipe(map((stages: IStage[]) => this.stageService.addStageToCollectionIfMissing<IStage>(stages, this.seat?.stage)))
      .subscribe((stages: IStage[]) => (this.stagesSharedCollection = stages));
  }
}
