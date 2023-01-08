package vn.tram.ticket.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.tram.ticket.domain.EventType;

/**
 * Service Interface for managing {@link EventType}.
 */
public interface EventTypeService {
    /**
     * Save a eventType.
     *
     * @param eventType the entity to save.
     * @return the persisted entity.
     */
    EventType save(EventType eventType);

    /**
     * Updates a eventType.
     *
     * @param eventType the entity to update.
     * @return the persisted entity.
     */
    EventType update(EventType eventType);

    /**
     * Partially updates a eventType.
     *
     * @param eventType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventType> partialUpdate(EventType eventType);

    /**
     * Get all the eventTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EventType> findAll(Pageable pageable);

    /**
     * Get the "id" eventType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventType> findOne(Long id);

    /**
     * Delete the "id" eventType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
