import dayjs from 'dayjs/esm';
import { IStage } from 'app/entities/stage/stage.model';

export interface IEvent {
  id: number;
  name?: string | null;
  description?: string | null;
  ageRestriction?: number | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  dateBefore?: dayjs.Dayjs | null;
  stage?: Pick<IStage, 'id'> | null;
}

export type NewEvent = Omit<IEvent, 'id'> & { id: null };
