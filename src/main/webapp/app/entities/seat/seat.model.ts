import { IStage } from 'app/entities/stage/stage.model';

export interface ISeat {
  id: number;
  row?: string | null;
  col?: number | null;
  seatClass?: string | null;
  stage?: Pick<IStage, 'id'> | null;
}

export type NewSeat = Omit<ISeat, 'id'> & { id: null };
