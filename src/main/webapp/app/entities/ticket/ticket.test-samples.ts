import { ITicket, NewTicket } from './ticket.model';

export const sampleWithRequiredData: ITicket = {
  id: 65582,
};

export const sampleWithPartialData: ITicket = {
  id: 9016,
  price: 47683,
};

export const sampleWithFullData: ITicket = {
  id: 93545,
  price: 23104,
};

export const sampleWithNewData: NewTicket = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
