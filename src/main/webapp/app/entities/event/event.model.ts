import { EventStatus } from 'app/entities/enumerations/event-status.model';
import { IEventType } from 'app/entities/event-type/event-type.model';
import dayjs from 'dayjs/esm';
import { IStage } from '../stage/stage.model';

export interface IEvent {
  id: number;
  name?: string | null;
  description?: string | null;
  ageRestriction?: number | null;
  status?: EventStatus | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  dateBefore?: dayjs.Dayjs | null;
  eventType?: IEventType | null;
  stage?: IStage | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
