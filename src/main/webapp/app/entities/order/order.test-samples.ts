import dayjs from 'dayjs/esm';

import { OrderStatus } from 'app/entities/enumerations/order-status.model';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 47761,
};

export const sampleWithPartialData: IOrder = {
  id: 34054,
  transactionCode: 'Texas Franc',
  isPaid: false,
  issuedDate: dayjs('2023-01-07T18:38'),
};

export const sampleWithFullData: IOrder = {
  id: 44858,
  status: OrderStatus['CANCELLED'],
  transactionCode: 'Account Cambridgeshire',
  isPaid: true,
  issuedDate: dayjs('2023-01-08T11:20'),
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
