import { IUser } from 'app/entities/user/user.model';

export interface IAppUser {
  id: number;
  user?: Pick<IUser, 'id'> | null;
}

export type NewAppUser = Omit<IAppUser, 'id'> & { id: null };
