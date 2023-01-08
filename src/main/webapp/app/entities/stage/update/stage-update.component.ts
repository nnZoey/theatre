import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { StageFormService, StageFormGroup } from './stage-form.service';
import { IStage } from '../stage.model';
import { StageService } from '../service/stage.service';

@Component({
  selector: 'jhi-stage-update',
  templateUrl: './stage-update.component.html',
})
export class StageUpdateComponent implements OnInit {
  isSaving = false;
  stage: IStage | null = null;

  editForm: StageFormGroup = this.stageFormService.createStageFormGroup();

  constructor(
    protected stageService: StageService,
    protected stageFormService: StageFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stage }) => {
      this.stage = stage;
      if (stage) {
        this.updateForm(stage);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stage = this.stageFormService.getStage(this.editForm);
    if (stage.id !== null) {
      this.subscribeToSaveResponse(this.stageService.update(stage));
    } else {
      this.subscribeToSaveResponse(this.stageService.create(stage));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStage>>): void {
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

  protected updateForm(stage: IStage): void {
    this.stage = stage;
    this.stageFormService.resetForm(this.editForm, stage);
  }
}
