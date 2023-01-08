package vn.tram.ticket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link vn.tram.ticket.domain.Seat} entity. This class is used
 * in {@link vn.tram.ticket.web.rest.SeatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /seats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter row;

    private LongFilter col;

    private StringFilter seatClass;

    private LongFilter ticketId;

    private LongFilter stageId;

    private Boolean distinct;

    public SeatCriteria() {}

    public SeatCriteria(SeatCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.row = other.row == null ? null : other.row.copy();
        this.col = other.col == null ? null : other.col.copy();
        this.seatClass = other.seatClass == null ? null : other.seatClass.copy();
        this.ticketId = other.ticketId == null ? null : other.ticketId.copy();
        this.stageId = other.stageId == null ? null : other.stageId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SeatCriteria copy() {
        return new SeatCriteria(this);
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

    public StringFilter getRow() {
        return row;
    }

    public StringFilter row() {
        if (row == null) {
            row = new StringFilter();
        }
        return row;
    }

    public void setRow(StringFilter row) {
        this.row = row;
    }

    public LongFilter getCol() {
        return col;
    }

    public LongFilter col() {
        if (col == null) {
            col = new LongFilter();
        }
        return col;
    }

    public void setCol(LongFilter col) {
        this.col = col;
    }

    public StringFilter getSeatClass() {
        return seatClass;
    }

    public StringFilter seatClass() {
        if (seatClass == null) {
            seatClass = new StringFilter();
        }
        return seatClass;
    }

    public void setSeatClass(StringFilter seatClass) {
        this.seatClass = seatClass;
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

    public LongFilter getStageId() {
        return stageId;
    }

    public LongFilter stageId() {
        if (stageId == null) {
            stageId = new LongFilter();
        }
        return stageId;
    }

    public void setStageId(LongFilter stageId) {
        this.stageId = stageId;
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
        final SeatCriteria that = (SeatCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(row, that.row) &&
            Objects.equals(col, that.col) &&
            Objects.equals(seatClass, that.seatClass) &&
            Objects.equals(ticketId, that.ticketId) &&
            Objects.equals(stageId, that.stageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, col, seatClass, ticketId, stageId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (row != null ? "row=" + row + ", " : "") +
            (col != null ? "col=" + col + ", " : "") +
            (seatClass != null ? "seatClass=" + seatClass + ", " : "") +
            (ticketId != null ? "ticketId=" + ticketId + ", " : "") +
            (stageId != null ? "stageId=" + stageId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
