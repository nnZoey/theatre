import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';

import { IEvent } from '../event.model';

import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter/filter.model';
import { EventDeleteDialogComponent } from '../delete/event-delete-dialog.component';
import { EntityArrayResponseType, EventService } from '../service/event.service';

@Component({
  selector: 'jhi-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss'],
})
export class EventComponent implements OnInit {
  initEvents?: IEvent[];
  searchedEvents?: IEvent[];
  isLoading = false;
  searchText?: string;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected eventService: EventService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IEvent): number => this.eventService.getEventIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));
  }

  delete(event: IEvent): void {
    const modalRef = this.modalService.open(EventDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.event = event;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  onSearchEvent(event: any): void {
    if (event.length >= 1) {
      const upperEvent = event.toUpperCase();
      this.searchedEvents = this.initEvents!.filter(element => {
        if (element.name?.toUpperCase() === upperEvent) {
          return true;
        }
        if (element.name!.toUpperCase().indexOf(upperEvent) >= 0) {
          return true;
        }
        return false;
      }).sort((a: any, b: any) => b.startTime - a.startTime);
    }
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending, this.filters.filterOptions))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
    this.filters.initializeFromParams(params);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.initEvents = dataFromBody;
    this.searchedEvents = this.initEvents;
  }

  protected fillComponentAttributesFromResponseBody(data: IEvent[] | null): IEvent[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(
    page?: number,
    predicate?: string,
    ascending?: boolean,
    filterOptions?: IFilterOption[]
  ): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    filterOptions?.forEach(filterOption => {
      queryObject[filterOption.name] = filterOption.values;
    });
    return this.eventService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean, filterOptions?: IFilterOption[]): void {
    const queryParamsObj: any = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    filterOptions?.forEach(filterOption => {
      queryParamsObj[filterOption.nameAsQueryParam()] = filterOption.values;
    });

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
