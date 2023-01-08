import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISeat } from '../seat.model';
import { SeatService } from '../service/seat.service';

@Injectable({ providedIn: 'root' })
export class SeatRoutingResolveService implements Resolve<ISeat | null> {
  constructor(protected service: SeatService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISeat | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((seat: HttpResponse<ISeat>) => {
          if (seat.body) {
            return of(seat.body);
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
