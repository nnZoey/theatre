package vn.tram.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Seat.
 */
@Entity
@Table(name = "seat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "row")
    private String row;

    @Column(name = "col")
    private Long col;

    @Column(name = "seat_class")
    private String seatClass;

    @JsonIgnoreProperties(value = { "seat", "order" }, allowSetters = true)
    @OneToOne(mappedBy = "seat")
    private Ticket ticket;

    @ManyToOne
    @JsonIgnoreProperties(value = { "seats", "events" }, allowSetters = true)
    private Stage stage;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRow() {
        return this.row;
    }

    public Seat row(String row) {
        this.setRow(row);
        return this;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public Long getCol() {
        return this.col;
    }

    public Seat col(Long col) {
        this.setCol(col);
        return this;
    }

    public void setCol(Long col) {
        this.col = col;
    }

    public String getSeatClass() {
        return this.seatClass;
    }

    public Seat seatClass(String seatClass) {
        this.setSeatClass(seatClass);
        return this;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public Ticket getTicket() {
        return this.ticket;
    }

    public void setTicket(Ticket ticket) {
        if (this.ticket != null) {
            this.ticket.setSeat(null);
        }
        if (ticket != null) {
            ticket.setSeat(this);
        }
        this.ticket = ticket;
    }

    public Seat ticket(Ticket ticket) {
        this.setTicket(ticket);
        return this;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Seat stage(Stage stage) {
        this.setStage(stage);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        return id != null && id.equals(((Seat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seat{" +
            "id=" + getId() +
            ", row='" + getRow() + "'" +
            ", col=" + getCol() +
            ", seatClass='" + getSeatClass() + "'" +
            "}";
    }
}
