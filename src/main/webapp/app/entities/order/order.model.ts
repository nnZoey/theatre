import dayjs from 'dayjs/esm';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IEvent } from 'app/entities/event/event.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IOrder {
  id: number;
  status?: Status | null;
  transactionCode?: string | null;
  isPaid?: boolean | null;
  issuedDate?: dayjs.Dayjs | null;
  appUser?: Pick<IAppUser, 'id'> | null;
  event?: Pick<IEvent, 'id'> | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
