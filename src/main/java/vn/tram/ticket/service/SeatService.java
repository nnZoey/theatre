package vn.tram.ticket.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.tram.ticket.domain.Seat;

/**
 * Service Interface for managing {@link Seat}.
 */
public interface SeatService {
    /**
     * Save a seat.
     *
     * @param seat the entity to save.
     * @return the persisted entity.
     */
    Seat save(Seat seat);

    /**
     * Updates a seat.
     *
     * @param seat the entity to update.
     * @return the persisted entity.
     */
    Seat update(Seat seat);

    /**
     * Partially updates a seat.
     *
     * @param seat the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Seat> partialUpdate(Seat seat);

    /**
     * Get all the seats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Seat> findAll(Pageable pageable);
    /**
     * Get all the Seat where Ticket is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Seat> findAllWhereTicketIsNull();

    /**
     * Get the "id" seat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Seat> findOne(Long id);

    /**
     * Delete the "id" seat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
