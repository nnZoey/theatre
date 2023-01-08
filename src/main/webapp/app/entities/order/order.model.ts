import { IAppUser } from 'app/entities/app-user/app-user.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { IEvent } from 'app/entities/event/event.model';
import dayjs from 'dayjs/esm';

export interface IOrder {
  id: number;
  status?: OrderStatus | null;
  transactionCode?: string | null;
  isPaid?: boolean | null;
  issuedDate?: dayjs.Dayjs | null;
  appUser?: IAppUser | null;
  event?: IEvent | null;
}

export type NewOrder = Omit<IOrder, 'id'> & { id: null };
