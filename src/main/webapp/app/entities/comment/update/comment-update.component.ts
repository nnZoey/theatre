import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CommentFormService, CommentFormGroup } from './comment-form.service';
import { IComment } from '../comment.model';
import { CommentService } from '../service/comment.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IEvent } from 'app/entities/event/event.model';
import { EventService } from 'app/entities/event/service/event.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;
  comment: IComment | null = null;

  appUsersSharedCollection: IAppUser[] = [];
  eventsSharedCollection: IEvent[] = [];

  editForm: CommentFormGroup = this.commentFormService.createCommentFormGroup();

  constructor(
    protected commentService: CommentService,
    protected commentFormService: CommentFormService,
    protected appUserService: AppUserService,
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAppUser = (o1: IAppUser | null, o2: IAppUser | null): boolean => this.appUserService.compareAppUser(o1, o2);

  compareEvent = (o1: IEvent | null, o2: IEvent | null): boolean => this.eventService.compareEvent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.comment = comment;
      if (comment) {
        this.updateForm(comment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.commentFormService.getComment(this.editForm);
    if (comment.id !== null) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(comment: IComment): void {
    this.comment = comment;
    this.commentFormService.resetForm(this.editForm, comment);

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(
      this.appUsersSharedCollection,
      comment.appUser
    );
    this.eventsSharedCollection = this.eventService.addEventToCollectionIfMissing<IEvent>(this.eventsSharedCollection, comment.event);
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing<IAppUser>(appUsers, this.comment?.appUser)))
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));

    this.eventService
      .query()
      .pipe(map((res: HttpResponse<IEvent[]>) => res.body ?? []))
      .pipe(map((events: IEvent[]) => this.eventService.addEventToCollectionIfMissing<IEvent>(events, this.comment?.event)))
      .subscribe((events: IEvent[]) => (this.eventsSharedCollection = events));
  }
}
