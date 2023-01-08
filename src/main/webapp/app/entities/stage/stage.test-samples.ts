import { IStage, NewStage } from './stage.model';

export const sampleWithRequiredData: IStage = {
  id: 86802,
};

export const sampleWithPartialData: IStage = {
  id: 53943,
};

export const sampleWithFullData: IStage = {
  id: 96589,
  name: 'B2B Chief National',
  location: 'channels Auto',
};

export const sampleWithNewData: NewStage = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
