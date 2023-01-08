import dayjs from 'dayjs/esm';

import { EventStatus } from 'app/entities/enumerations/event-status.model';

import { IEvent, NewEvent } from './event.model';

export const sampleWithRequiredData: IEvent = {
  id: 63022,
};

export const sampleWithPartialData: IEvent = {
  id: 2671,
  ageRestriction: 14349,
  startTime: dayjs('2023-01-08T09:29'),
  dateBefore: dayjs('2023-01-08T14:12'),
};

export const sampleWithFullData: IEvent = {
  id: 28577,
  name: 'Maryland withdrawal indigo',
  description: 'Account',
  ageRestriction: 82454,
  status: EventStatus['PENDING'],
  startTime: dayjs('2023-01-07T15:09'),
  endTime: dayjs('2023-01-08T10:33'),
  dateBefore: dayjs('2023-01-07T22:25'),
  image: '../fake-data/blob/hipster.png',
  imageContentType: 'unknown',
};

export const sampleWithNewData: NewEvent = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
