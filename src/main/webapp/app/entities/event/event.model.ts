import dayjs from 'dayjs/esm';
import { IEventType } from 'app/entities/event-type/event-type.model';
import { IStage } from 'app/entities/stage/stage.model';
import { EventStatus } from 'app/entities/enumerations/event-status.model';

export interface IEvent {
  id: number;
  name?: string | null;
  description?: string | null;
  ageRestriction?: number | null;
  status?: EventStatus | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  dateBefore?: dayjs.Dayjs | null;
  eventType?: Pick<IEventType, 'id'> | null;
  stage?: Pick<IStage, 'id'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
