export interface IEventType {
  id: number;
  name?: string | null;
}

export type NewEventType = Omit<IEventType, 'id'> & { id: null };
