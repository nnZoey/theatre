import dayjs from 'dayjs/esm';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
};

export const sampleWithPartialData: IEvent = {
  id: 50740,
  ageRestriction: 13579,
  endTime: dayjs('2023-01-08T05:00'),
};

export const sampleWithFullData: IEvent = {
  id: 14349,
  name: 'Home',
  description: 'transform one-to-one',
  ageRestriction: 78603,
  startTime: dayjs('2023-01-07T13:15'),
  endTime: dayjs('2023-01-08T02:16'),
  dateBefore: dayjs('2023-01-07T15:21'),
};

export const sampleWithNewData: NewEvent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
