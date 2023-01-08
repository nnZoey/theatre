import { IEvent } from 'app/entities/event/event.model';

export interface IEventType {
  id: number;
  name?: string | null;
  event?: Pick<IEvent, 'id'> | null;
}

export type NewEventType = Omit<IEventType, 'id'> & { id: null };
