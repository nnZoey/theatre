import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';

import { IOrder } from '../order.model';

import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { FilterOptions, IFilterOption, IFilterOptions } from 'app/shared/filter/filter.model';
import { OrderDeleteDialogComponent } from '../delete/order-delete-dialog.component';
import { EntityArrayResponseType, OrderService } from '../service/order.service';

@Component({
  selector: 'jhi-order',
  templateUrl: './order.component.html',
})
export class OrderComponent implements OnInit {
  orders?: IOrder[];
  isLoading = false;

  predicate = 'id';
  ascending = true;
  filters: IFilterOptions = new FilterOptions();

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  constructor(
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected accountService: AccountService,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IOrder): number => this.orderService.getOrderIdentifier(item);

  ngOnInit(): void {
    this.load();

    this.filters.filterChanges.subscribe(filterOptions => this.handleNavigation(1, this.predicate, this.ascending, filterOptions));

    this.accountService.getCurrentAppUser().subscribe(appUser => {
      if (this.accountService.hasAnyAuthority(['ROLE_USER'])) {
        this.filters.addFilter('appUserId.equals', String(appUser.id));
      }
    });

    this.router.events.subscribe(() => {
      this.accountService.getCurrentAppUser().subscribe(appUser => {
        if (this.accountService.hasAnyAuthority(['ROLE_USER'])) {
          this.filters.addFilter('appUserId.equals', String(appUser.id));
        }
      });
    });
  }

  delete(order: IOrder): void {
    const modalRef = this.modalService.open(OrderDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.order = order;
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

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending, this.filters.filterOptions);
  }

  getBadgeClass(order: IOrder): string {
    switch (order.status) {
      case OrderStatus.PAID:
        return 'badge text-bg-success';
      case OrderStatus.CANCELLED:
        return 'badge text-bg-danger';
      case OrderStatus.PENDING:
        return 'badge text-bg-warning';
      default:
        return '';
    }
  }

  approve(order: IOrder): void {
    order.status = OrderStatus.PAID;
    this.orderService.partialUpdate(order).subscribe({
      next: () => {
        this.load();
      },
    });
  }

  reject(order: IOrder): void {
    order.status = OrderStatus.CANCELLED;
    this.orderService.partialUpdate(order).subscribe({
      next: () => {
        this.load();
      },
    });
  }

  getApproveButtonRendered(order: IOrder): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_SELLER']) && order.status !== OrderStatus.PAID;
  }

  getRejectButtonRendered(order: IOrder): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_SELLER']) && order.status !== OrderStatus.CANCELLED;
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
    this.orders = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IOrder[] | null): IOrder[] {
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
    return this.orderService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
