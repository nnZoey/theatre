package vn.tram.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import vn.tram.ticket.domain.enumeration.OrderStatus;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "issued_date")
    private Instant issuedDate;

    @OneToMany(mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "seat", "order" }, allowSetters = true)
    private Set<Ticket> tickets = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "comments", "events" }, allowSetters = true)
    private AppUser appUser;

    @ManyToOne
    @JsonIgnoreProperties(value = { "orders", "comments", "eventType", "stage", "createdBy" }, allowSetters = true)
    private Event event;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public Order status(OrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getTransactionCode() {
        return this.transactionCode;
    }

    public Order transactionCode(String transactionCode) {
        this.setTransactionCode(transactionCode);
        return this;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public Boolean getIsPaid() {
        return this.isPaid;
    }

    public Order isPaid(Boolean isPaid) {
        this.setIsPaid(isPaid);
        return this;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Instant getIssuedDate() {
        return this.issuedDate;
    }

    public Order issuedDate(Instant issuedDate) {
        this.setIssuedDate(issuedDate);
        return this;
    }

    public void setIssuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Set<Ticket> getTickets() {
        return this.tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        if (this.tickets != null) {
            this.tickets.forEach(i -> i.setOrder(null));
        }
        if (tickets != null) {
            tickets.forEach(i -> i.setOrder(this));
        }
        this.tickets = tickets;
    }

    public Order tickets(Set<Ticket> tickets) {
        this.setTickets(tickets);
        return this;
    }

    public Order addTicket(Ticket ticket) {
        this.tickets.add(ticket);
        ticket.setOrder(this);
        return this;
    }

    public Order removeTicket(Ticket ticket) {
        this.tickets.remove(ticket);
        ticket.setOrder(null);
        return this;
    }

    public AppUser getAppUser() {
        return this.appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Order appUser(AppUser appUser) {
        this.setAppUser(appUser);
        return this;
    }

    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Order event(Event event) {
        this.setEvent(event);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", transactionCode='" + getTransactionCode() + "'" +
            ", isPaid='" + getIsPaid() + "'" +
            ", issuedDate='" + getIssuedDate() + "'" +
            "}";
    }
}
