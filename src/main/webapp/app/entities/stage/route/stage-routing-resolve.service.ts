import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStage } from '../stage.model';
import { StageService } from '../service/stage.service';

@Injectable({ providedIn: 'root' })
export class StageRoutingResolveService implements Resolve<IStage | null> {
  constructor(protected service: StageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStage | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((stage: HttpResponse<IStage>) => {
          if (stage.body) {
            return of(stage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
