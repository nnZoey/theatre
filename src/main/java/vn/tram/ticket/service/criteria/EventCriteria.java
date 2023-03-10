package vn.tram.ticket.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import vn.tram.ticket.domain.enumeration.EventStatus;

/**
 * Criteria class for the {@link vn.tram.ticket.domain.Event} entity. This class is used
 * in {@link vn.tram.ticket.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EventStatus
     */
    public static class EventStatusFilter extends Filter<EventStatus> {

        public EventStatusFilter() {}

        public EventStatusFilter(EventStatusFilter filter) {
            super(filter);
        }

        @Override
        public EventStatusFilter copy() {
            return new EventStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private LongFilter ageRestriction;

    private EventStatusFilter status;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private InstantFilter dateBefore;

    private LongFilter orderId;

    private LongFilter commentId;

    private LongFilter eventTypeId;

    private LongFilter stageId;

    private LongFilter createdById;

    private Boolean distinct;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.ageRestriction = other.ageRestriction == null ? null : other.ageRestriction.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.dateBefore = other.dateBefore == null ? null : other.dateBefore.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.commentId = other.commentId == null ? null : other.commentId.copy();
        this.eventTypeId = other.eventTypeId == null ? null : other.eventTypeId.copy();
        this.stageId = other.stageId == null ? null : other.stageId.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getAgeRestriction() {
        return ageRestriction;
    }

    public LongFilter ageRestriction() {
        if (ageRestriction == null) {
            ageRestriction = new LongFilter();
        }
        return ageRestriction;
    }

    public void setAgeRestriction(LongFilter ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public EventStatusFilter getStatus() {
        return status;
    }

    public EventStatusFilter status() {
        if (status == null) {
            status = new EventStatusFilter();
        }
        return status;
    }

    public void setStatus(EventStatusFilter status) {
        this.status = status;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public InstantFilter getDateBefore() {
        return dateBefore;
    }

    public InstantFilter dateBefore() {
        if (dateBefore == null) {
            dateBefore = new InstantFilter();
        }
        return dateBefore;
    }

    public void setDateBefore(InstantFilter dateBefore) {
        this.dateBefore = dateBefore;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public LongFilter getCommentId() {
        return commentId;
    }

    public LongFilter commentId() {
        if (commentId == null) {
            commentId = new LongFilter();
        }
        return commentId;
    }

    public void setCommentId(LongFilter commentId) {
        this.commentId = commentId;
    }

    public LongFilter getEventTypeId() {
        return eventTypeId;
    }

    public LongFilter eventTypeId() {
        if (eventTypeId == null) {
            eventTypeId = new LongFilter();
        }
        return eventTypeId;
    }

    public void setEventTypeId(LongFilter eventTypeId) {
        this.eventTypeId = eventTypeId;
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

    public LongFilter getCreatedById() {
        return createdById;
    }

    public LongFilter createdById() {
        if (createdById == null) {
            createdById = new LongFilter();
        }
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
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
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(ageRestriction, that.ageRestriction) &&
            Objects.equals(status, that.status) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(dateBefore, that.dateBefore) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(commentId, that.commentId) &&
            Objects.equals(eventTypeId, that.eventTypeId) &&
            Objects.equals(stageId, that.stageId) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            ageRestriction,
            status,
            startTime,
            endTime,
            dateBefore,
            orderId,
            commentId,
            eventTypeId,
            stageId,
            createdById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (ageRestriction != null ? "ageRestriction=" + ageRestriction + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (dateBefore != null ? "dateBefore=" + dateBefore + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (commentId != null ? "commentId=" + commentId + ", " : "") +
            (eventTypeId != null ? "eventTypeId=" + eventTypeId + ", " : "") +
            (stageId != null ? "stageId=" + stageId + ", " : "") +
            (createdById != null ? "createdById=" + createdById + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
