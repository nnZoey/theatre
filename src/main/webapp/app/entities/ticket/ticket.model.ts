import { IOrder } from 'app/entities/order/order.model';
import { ISeat } from 'app/entities/seat/seat.model';

export interface ITicket {
  id: number;
  price?: number | null;
  seat?: ISeat | null;
  order?: Pick<IOrder, 'id'> | null;
}

export type NewTicket = Omit<ITicket, 'id'> & { id: null };
