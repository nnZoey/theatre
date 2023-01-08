import { IEventType, NewEventType } from './event-type.model';

export const sampleWithRequiredData: IEventType = {
  id: 47,
};

export const sampleWithPartialData: IEventType = {
  id: 12631,
  name: 'EXE Ariary',
};

export const sampleWithFullData: IEventType = {
  id: 25032,
  name: 'HTTP',
};

export const sampleWithNewData: NewEventType = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
