import { IAppUser } from 'app/entities/app-user/app-user.model';
import { IEvent } from 'app/entities/event/event.model';

export interface IComment {
  id: number;
  content?: string | null;
  appUser?: Pick<IAppUser, 'id'> | null;
  event?: Pick<IEvent, 'id'> | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
