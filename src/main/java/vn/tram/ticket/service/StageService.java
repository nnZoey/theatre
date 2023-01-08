package vn.tram.ticket.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.tram.ticket.domain.Stage;

/**
 * Service Interface for managing {@link Stage}.
 */
public interface StageService {
    /**
     * Save a stage.
     *
     * @param stage the entity to save.
     * @return the persisted entity.
     */
    Stage save(Stage stage);

    /**
     * Updates a stage.
     *
     * @param stage the entity to update.
     * @return the persisted entity.
     */
    Stage update(Stage stage);

    /**
     * Partially updates a stage.
     *
     * @param stage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Stage> partialUpdate(Stage stage);

    /**
     * Get all the stages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Stage> findAll(Pageable pageable);

    /**
     * Get the "id" stage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Stage> findOne(Long id);

    /**
     * Delete the "id" stage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
