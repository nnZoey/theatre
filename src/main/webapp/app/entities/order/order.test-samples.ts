import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IOrder, NewOrder } from './order.model';

export const sampleWithRequiredData: IOrder = {
  id: 47761,
};

export const sampleWithPartialData: IOrder = {
  id: 34054,
  transactionCode: 'Texas Franc',
  isPaid: false,
  issuedDate: dayjs('2023-01-07T09:55'),
};

export const sampleWithFullData: IOrder = {
  id: 44858,
  status: Status['PENDING'],
  transactionCode: 'Account Cambridgeshire',
  isPaid: true,
  issuedDate: dayjs('2023-01-08T02:37'),
};

export const sampleWithNewData: NewOrder = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
