import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEventType, NewEventType } from '../event-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEventType for edit and NewEventTypeFormGroupInput for create.
 */
type EventTypeFormGroupInput = IEventType | PartialWithRequiredKeyOf<NewEventType>;

type EventTypeFormDefaults = Pick<NewEventType, 'id'>;

type EventTypeFormGroupContent = {
  id: FormControl<IEventType['id'] | NewEventType['id']>;
  name: FormControl<IEventType['name']>;
  event: FormControl<IEventType['event']>;
};

export type EventTypeFormGroup = FormGroup<EventTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EventTypeFormService {
  createEventTypeFormGroup(eventType: EventTypeFormGroupInput = { id: null }): EventTypeFormGroup {
    const eventTypeRawValue = {
      ...this.getFormDefaults(),
      ...eventType,
    };
    return new FormGroup<EventTypeFormGroupContent>({
      id: new FormControl(
        { value: eventTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(eventTypeRawValue.name),
      event: new FormControl(eventTypeRawValue.event),
    });
  }

  getEventType(form: EventTypeFormGroup): IEventType | NewEventType {
    return form.getRawValue() as IEventType | NewEventType;
  }

  resetForm(form: EventTypeFormGroup, eventType: EventTypeFormGroupInput): void {
    const eventTypeRawValue = { ...this.getFormDefaults(), ...eventType };
    form.reset(
      {
        ...eventTypeRawValue,
        id: { value: eventTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EventTypeFormDefaults {
    return {
      id: null,
    };
  }
}
