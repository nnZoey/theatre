import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStage, NewStage } from '../stage.model';

export type PartialUpdateStage = Partial<IStage> & Pick<IStage, 'id'>;

export type EntityResponseType = HttpResponse<IStage>;
export type EntityArrayResponseType = HttpResponse<IStage[]>;

@Injectable({ providedIn: 'root' })
export class StageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(stage: NewStage): Observable<EntityResponseType> {
    return this.http.post<IStage>(this.resourceUrl, stage, { observe: 'response' });
  }

  update(stage: IStage): Observable<EntityResponseType> {
    return this.http.put<IStage>(`${this.resourceUrl}/${this.getStageIdentifier(stage)}`, stage, { observe: 'response' });
  }

  partialUpdate(stage: PartialUpdateStage): Observable<EntityResponseType> {
    return this.http.patch<IStage>(`${this.resourceUrl}/${this.getStageIdentifier(stage)}`, stage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStageIdentifier(stage: Pick<IStage, 'id'>): number {
    return stage.id;
  }

  compareStage(o1: Pick<IStage, 'id'> | null, o2: Pick<IStage, 'id'> | null): boolean {
    return o1 && o2 ? this.getStageIdentifier(o1) === this.getStageIdentifier(o2) : o1 === o2;
  }

  addStageToCollectionIfMissing<Type extends Pick<IStage, 'id'>>(
    stageCollection: Type[],
    ...stagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stages: Type[] = stagesToCheck.filter(isPresent);
    if (stages.length > 0) {
      const stageCollectionIdentifiers = stageCollection.map(stageItem => this.getStageIdentifier(stageItem)!);
      const stagesToAdd = stages.filter(stageItem => {
        const stageIdentifier = this.getStageIdentifier(stageItem);
        if (stageCollectionIdentifiers.includes(stageIdentifier)) {
          return false;
        }
        stageCollectionIdentifiers.push(stageIdentifier);
        return true;
      });
      return [...stagesToAdd, ...stageCollection];
    }
    return stageCollection;
  }
}
