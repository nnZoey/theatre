package vn.tram.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppUser.
 */
@Entity
@Table(name = "app_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tickets", "appUser", "event" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "appUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "appUser", "event" }, allowSetters = true)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "createdBy")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orders", "comments", "eventType", "stage", "createdBy" }, allowSetters = true)
    private Set<Event> events = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AppUser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setAppUser(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setAppUser(this));
        }
        this.orders = orders;
    }

    public AppUser orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public AppUser addOrder(Order order) {
        this.orders.add(order);
        order.setAppUser(this);
        return this;
    }

    public AppUser removeOrder(Order order) {
        this.orders.remove(order);
        order.setAppUser(null);
        return this;
    }

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        if (this.comments != null) {
            this.comments.forEach(i -> i.setAppUser(null));
        }
        if (comments != null) {
            comments.forEach(i -> i.setAppUser(this));
        }
        this.comments = comments;
    }

    public AppUser comments(Set<Comment> comments) {
        this.setComments(comments);
        return this;
    }

    public AppUser addComment(Comment comment) {
        this.comments.add(comment);
        comment.setAppUser(this);
        return this;
    }

    public AppUser removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setAppUser(null);
        return this;
    }

    public Set<Event> getEvents() {
        return this.events;
    }

    public void setEvents(Set<Event> events) {
        if (this.events != null) {
            this.events.forEach(i -> i.setCreatedBy(null));
        }
        if (events != null) {
            events.forEach(i -> i.setCreatedBy(this));
        }
        this.events = events;
    }

    public AppUser events(Set<Event> events) {
        this.setEvents(events);
        return this;
    }

    public AppUser addEvent(Event event) {
        this.events.add(event);
        event.setCreatedBy(this);
        return this;
    }

    public AppUser removeEvent(Event event) {
        this.events.remove(event);
        event.setCreatedBy(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppUser)) {
            return false;
        }
        return id != null && id.equals(((AppUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUser{" +
            "id=" + getId() +
            "}";
    }
}
