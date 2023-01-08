import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EventFormService, EventFormGroup } from './event-form.service';
import { IEvent } from '../event.model';
import { EventService } from '../service/event.service';
import { IEventType } from 'app/entities/event-type/event-type.model';
import { EventTypeService } from 'app/entities/event-type/service/event-type.service';
import { IStage } from 'app/entities/stage/stage.model';
import { StageService } from 'app/entities/stage/service/stage.service';
import { EventStatus } from 'app/entities/enumerations/event-status.model';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html',
})
export class EventUpdateComponent implements OnInit {
  isSaving = false;
  event: IEvent | null = null;
  eventStatusValues = Object.keys(EventStatus);

  eventTypesSharedCollection: IEventType[] = [];
  stagesSharedCollection: IStage[] = [];

  editForm: EventFormGroup = this.eventFormService.createEventFormGroup();

  constructor(
    protected eventService: EventService,
    protected eventFormService: EventFormService,
    protected eventTypeService: EventTypeService,
    protected stageService: StageService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEventType = (o1: IEventType | null, o2: IEventType | null): boolean => this.eventTypeService.compareEventType(o1, o2);

  compareStage = (o1: IStage | null, o2: IStage | null): boolean => this.stageService.compareStage(o1, o2);

  ngOnInit(): void {
    console.warn(this.eventStatusValues);
    this.activatedRoute.data.subscribe(({ event }) => {
      this.event = event;
      if (event) {
        this.updateForm(event);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const event = this.eventFormService.getEvent(this.editForm);
    if (event.id !== null) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>): void {
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

  protected updateForm(event: IEvent): void {
    this.event = event;
    this.eventFormService.resetForm(this.editForm, event);

    this.eventTypesSharedCollection = this.eventTypeService.addEventTypeToCollectionIfMissing<IEventType>(
      this.eventTypesSharedCollection,
      event.eventType
    );
    this.stagesSharedCollection = this.stageService.addStageToCollectionIfMissing<IStage>(this.stagesSharedCollection, event.stage);
  }

  protected loadRelationshipsOptions(): void {
    this.eventTypeService
      .query()
      .pipe(map((res: HttpResponse<IEventType[]>) => res.body ?? []))
      .pipe(
        map((eventTypes: IEventType[]) =>
          this.eventTypeService.addEventTypeToCollectionIfMissing<IEventType>(eventTypes, this.event?.eventType)
        )
      )
      .subscribe((eventTypes: IEventType[]) => (this.eventTypesSharedCollection = eventTypes));

    this.stageService
      .query()
      .pipe(map((res: HttpResponse<IStage[]>) => res.body ?? []))
      .pipe(map((stages: IStage[]) => this.stageService.addStageToCollectionIfMissing<IStage>(stages, this.event?.stage)))
      .subscribe((stages: IStage[]) => (this.stagesSharedCollection = stages));
  }
}
