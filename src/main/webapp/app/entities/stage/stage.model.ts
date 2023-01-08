export interface IStage {
  id: number;
  name?: string | null;
  location?: string | null;
}

export type NewStage = Omit<IStage, 'id'> & { id: null };
