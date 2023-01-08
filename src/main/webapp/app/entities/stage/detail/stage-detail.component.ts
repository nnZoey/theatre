import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStage } from '../stage.model';

@Component({
  selector: 'jhi-stage-detail',
  templateUrl: './stage-detail.component.html',
})
export class StageDetailComponent implements OnInit {
  stage: IStage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stage }) => {
      this.stage = stage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
