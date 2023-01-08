import { ISeat, NewSeat } from './seat.model';

export const sampleWithRequiredData: ISeat = {
  id: 43988,
};

export const sampleWithPartialData: ISeat = {
  id: 68289,
  row: 'Jewelery matrix projection',
  seatClass: 'pink',
};

export const sampleWithFullData: ISeat = {
  id: 13479,
  row: 'Borders static',
  col: 71771,
  seatClass: 'streamline ivory',
};

export const sampleWithNewData: NewSeat = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
