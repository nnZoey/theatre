import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvent, NewEvent } from '../event.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvent for edit and NewEventFormGroupInput for create.
 */
type EventFormGroupInput = IEvent | PartialWithRequiredKeyOf<NewEvent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvent | NewEvent> = Omit<T, 'startTime' | 'endTime' | 'dateBefore'> & {
  startTime?: string | null;
  endTime?: string | null;
  dateBefore?: string | null;
};

type EventFormRawValue = FormValueOf<IEvent>;

type NewEventFormRawValue = FormValueOf<NewEvent>;

type EventFormDefaults = Pick<NewEvent, 'id' | 'startTime' | 'endTime' | 'dateBefore'>;

type EventFormGroupContent = {
  id: FormControl<EventFormRawValue['id'] | NewEvent['id']>;
  name: FormControl<EventFormRawValue['name']>;
  description: FormControl<EventFormRawValue['description']>;
  ageRestriction: FormControl<EventFormRawValue['ageRestriction']>;
  status: FormControl<EventFormRawValue['status']>;
  startTime: FormControl<EventFormRawValue['startTime']>;
  endTime: FormControl<EventFormRawValue['endTime']>;
  dateBefore: FormControl<EventFormRawValue['dateBefore']>;
  eventType: FormControl<EventFormRawValue['eventType']>;
  stage: FormControl<EventFormRawValue['stage']>;
};

export type EventFormGroup = FormGroup<EventFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventFormService {
  createEventFormGroup(event: EventFormGroupInput = { id: null }): EventFormGroup {
    const eventRawValue = this.convertEventToEventRawValue({
      ...this.getFormDefaults(),
      ...event,
    });
    return new FormGroup<EventFormGroupContent>({
      id: new FormControl(
        { value: eventRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(eventRawValue.name),
      description: new FormControl(eventRawValue.description),
      ageRestriction: new FormControl(eventRawValue.ageRestriction),
      status: new FormControl(eventRawValue.status),
      startTime: new FormControl(eventRawValue.startTime),
      endTime: new FormControl(eventRawValue.endTime),
      dateBefore: new FormControl(eventRawValue.dateBefore),
      eventType: new FormControl(eventRawValue.eventType),
      stage: new FormControl(eventRawValue.stage),
    });
  }

  getEvent(form: EventFormGroup): IEvent | NewEvent {
    return this.convertEventRawValueToEvent(form.getRawValue() as EventFormRawValue | NewEventFormRawValue);
  }

  resetForm(form: EventFormGroup, event: EventFormGroupInput): void {
    const eventRawValue = this.convertEventToEventRawValue({ ...this.getFormDefaults(), ...event });
    form.reset(
      {
        ...eventRawValue,
        id: { value: eventRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
      dateBefore: currentTime,
    };
  }

  private convertEventRawValueToEvent(rawEvent: EventFormRawValue | NewEventFormRawValue): IEvent | NewEvent {
    return {
      ...rawEvent,
      startTime: dayjs(rawEvent.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawEvent.endTime, DATE_TIME_FORMAT),
      dateBefore: dayjs(rawEvent.dateBefore, DATE_TIME_FORMAT),
    };
  }

  private convertEventToEventRawValue(
    event: IEvent | (Partial<NewEvent> & EventFormDefaults)
  ): EventFormRawValue | PartialWithRequiredKeyOf<NewEventFormRawValue> {
    return {
      ...event,
      startTime: event.startTime ? event.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: event.endTime ? event.endTime.format(DATE_TIME_FORMAT) : undefined,
      dateBefore: event.dateBefore ? event.dateBefore.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
