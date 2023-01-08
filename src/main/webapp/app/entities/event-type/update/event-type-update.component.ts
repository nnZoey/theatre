import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EventTypeFormService, EventTypeFormGroup } from './event-type-form.service';
import { IEventType } from '../event-type.model';
import { EventTypeService } from '../service/event-type.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

@Component({
  selector: 'jhi-event-type-update',
  templateUrl: './event-type-update.component.html',
})
export class EventTypeUpdateComponent implements OnInit {
  isSaving = false;
  eventType: IEventType | null = null;

  eventsSharedCollection: IEvent[] = [];

  editForm: EventTypeFormGroup = this.eventTypeFormService.createEventTypeFormGroup();

  constructor(
    protected eventTypeService: EventTypeService,
    protected eventTypeFormService: EventTypeFormService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEvent = (o1: IEvent | null, o2: IEvent | null): boolean => this.eventService.compareEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eventType }) => {
      this.eventType = eventType;
      if (eventType) {
        this.updateForm(eventType);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventType = this.eventTypeFormService.getEventType(this.editForm);
    if (eventType.id !== null) {
      this.subscribeToSaveResponse(this.eventTypeService.update(eventType));
    } else {
      this.subscribeToSaveResponse(this.eventTypeService.create(eventType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEventType>>): void {
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

  protected updateForm(eventType: IEventType): void {
    this.eventType = eventType;
    this.eventTypeFormService.resetForm(this.editForm, eventType);

    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing<IEvent>(this.eventsSharedCollection, eventType.event);
  }

  protected loadRelationshipsOptions(): void {
    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing<IEvent>(events, this.eventType?.event)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }
}
