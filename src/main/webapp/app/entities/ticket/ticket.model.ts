import { ISeat } from 'app/entities/seat/seat.model';
import { IOrder } from 'app/entities/order/order.model';

export interface ITicket {
  id: number;
  price?: number | null;
  seat?: Pick<ISeat, 'id'> | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewTicket = Omit<ITicket, 'id'> & { id: null };
