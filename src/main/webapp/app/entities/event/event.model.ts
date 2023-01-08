import { IAppUser } from 'app/entities/app-user/app-user.model';
import { EventStatus } from 'app/entities/enumerations/event-status.model';
import { IEventType } from 'app/entities/event-type/event-type.model';
import { IStage } from 'app/entities/stage/stage.model';
import dayjs from 'dayjs/esm';

export interface IEvent {
  id: number;
  name?: string | null;
  description?: string | null;
  ageRestriction?: number | null;
  status?: EventStatus | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  dateBefore?: dayjs.Dayjs | null;
  image?: string | null;
  imageContentType?: string | null;
  eventType?: IEventType | null;
  stage?: IStage | null;
  createdBy?: Pick<IAppUser, 'id'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
