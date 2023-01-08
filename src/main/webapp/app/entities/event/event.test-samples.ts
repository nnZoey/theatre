import dayjs from 'dayjs/esm';

import { EventStatus } from 'app/entities/enumerations/event-status.model';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
};

export const sampleWithPartialData: IEvent = {
  id: 13579,
  ageRestriction: 2671,
  startTime: dayjs('2023-01-08T05:07'),
  dateBefore: dayjs('2023-01-08T03:38'),
};

export const sampleWithFullData: IEvent = {
  id: 852,
  name: 'pixel',
  description: 'withdrawal',
  ageRestriction: 20747,
  status: EventStatus['REJECTED'],
  startTime: dayjs('2023-01-07T16:10'),
  endTime: dayjs('2023-01-08T05:11'),
  dateBefore: dayjs('2023-01-07T18:16'),
};

export const sampleWithNewData: NewEvent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
