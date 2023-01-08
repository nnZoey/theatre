package vn.tram.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.tram.ticket.domain.enumeration.EventStatus;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "age_restriction")
    private Long ageRestriction;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "date_before")
    private Instant dateBefore;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tickets", "appUser", "event" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "event" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "events" }, allowSetters = true)
    private EventType eventType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seats", "events" }, allowSetters = true)
    private Stage stage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Event id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Event name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Event description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAgeRestriction() {
        return this.ageRestriction;
    }

    public Event ageRestriction(Long ageRestriction) {
        this.setAgeRestriction(ageRestriction);
        return this;
    }

    public void setAgeRestriction(Long ageRestriction) {
        this.ageRestriction = ageRestriction;
    }

    public EventStatus getStatus() {
        return this.status;
    }

    public Event status(EventStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Event startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Event endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getDateBefore() {
        return this.dateBefore;
    }

    public Event dateBefore(Instant dateBefore) {
        this.setDateBefore(dateBefore);
        return this;
    }

    public void setDateBefore(Instant dateBefore) {
        this.dateBefore = dateBefore;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setEvent(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setEvent(this));
        }
        this.orders = orders;
    }

    public Event orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Event addOrder(Order order) {
        this.orders.add(order);
        order.setEvent(this);
        return this;
    }

    public Event removeOrder(Order order) {
        this.orders.remove(order);
        order.setEvent(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setEvent(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setEvent(this));
        }
        this.comments = comments;
    }

    public Event comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public Event addComment(Comment comment) {
        this.comments.add(comment);
        comment.setEvent(this);
        return this;
    }

    public Event removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setEvent(null);
        return this;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Event eventType(EventType eventType) {
        this.setEventType(eventType);
        return this;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Event stage(Stage stage) {
        this.setStage(stage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", ageRestriction=" + getAgeRestriction() +
            ", status='" + getStatus() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", dateBefore='" + getDateBefore() + "'" +
            "}";
    }
}
