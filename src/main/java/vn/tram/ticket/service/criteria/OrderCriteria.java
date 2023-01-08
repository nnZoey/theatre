package vn.tram.ticket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import vn.tram.ticket.domain.enumeration.OrderStatus;

/**
 * Criteria class for the {@link vn.tram.ticket.domain.Order} entity. This class is used
 * in {@link vn.tram.ticket.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OrderStatus
     */
    public static class OrderStatusFilter extends Filter<OrderStatus> {

        public OrderStatusFilter() {}

        public OrderStatusFilter(OrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public OrderStatusFilter copy() {
            return new OrderStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private OrderStatusFilter status;

    private StringFilter transactionCode;

    private BooleanFilter isPaid;

    private InstantFilter issuedDate;

    private LongFilter ticketId;

    private LongFilter appUserId;

    private LongFilter eventId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.transactionCode = other.transactionCode == null ? null : other.transactionCode.copy();
        this.isPaid = other.isPaid == null ? null : other.isPaid.copy();
        this.issuedDate = other.issuedDate == null ? null : other.issuedDate.copy();
        this.ticketId = other.ticketId == null ? null : other.ticketId.copy();
        this.appUserId = other.appUserId == null ? null : other.appUserId.copy();
        this.eventId = other.eventId == null ? null : other.eventId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public OrderStatusFilter getStatus() {
        return status;
    }

    public OrderStatusFilter status() {
        if (status == null) {
            status = new OrderStatusFilter();
        }
        return status;
    }

    public void setStatus(OrderStatusFilter status) {
        this.status = status;
    }

    public StringFilter getTransactionCode() {
        return transactionCode;
    }

    public StringFilter transactionCode() {
        if (transactionCode == null) {
            transactionCode = new StringFilter();
        }
        return transactionCode;
    }

    public void setTransactionCode(StringFilter transactionCode) {
        this.transactionCode = transactionCode;
    }

    public BooleanFilter getIsPaid() {
        return isPaid;
    }

    public BooleanFilter isPaid() {
        if (isPaid == null) {
            isPaid = new BooleanFilter();
        }
        return isPaid;
    }

    public void setIsPaid(BooleanFilter isPaid) {
        this.isPaid = isPaid;
    }

    public InstantFilter getIssuedDate() {
        return issuedDate;
    }

    public InstantFilter issuedDate() {
        if (issuedDate == null) {
            issuedDate = new InstantFilter();
        }
        return issuedDate;
    }

    public void setIssuedDate(InstantFilter issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LongFilter getTicketId() {
        return ticketId;
    }

    public LongFilter ticketId() {
        if (ticketId == null) {
            ticketId = new LongFilter();
        }
        return ticketId;
    }

    public void setTicketId(LongFilter ticketId) {
        this.ticketId = ticketId;
    }

    public LongFilter getAppUserId() {
        return appUserId;
    }

    public LongFilter appUserId() {
        if (appUserId == null) {
            appUserId = new LongFilter();
        }
        return appUserId;
    }

    public void setAppUserId(LongFilter appUserId) {
        this.appUserId = appUserId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public LongFilter eventId() {
        if (eventId == null) {
            eventId = new LongFilter();
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(transactionCode, that.transactionCode) &&
            Objects.equals(isPaid, that.isPaid) &&
            Objects.equals(issuedDate, that.issuedDate) &&
            Objects.equals(ticketId, that.ticketId) &&
            Objects.equals(appUserId, that.appUserId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, transactionCode, isPaid, issuedDate, ticketId, appUserId, eventId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (transactionCode != null ? "transactionCode=" + transactionCode + ", " : "") +
            (isPaid != null ? "isPaid=" + isPaid + ", " : "") +
            (issuedDate != null ? "issuedDate=" + issuedDate + ", " : "") +
            (ticketId != null ? "ticketId=" + ticketId + ", " : "") +
            (appUserId != null ? "appUserId=" + appUserId + ", " : "") +
            (eventId != null ? "eventId=" + eventId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
