package vn.tram.ticket.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import vn.tram.ticket.domain.*; // for static metamodels
import vn.tram.ticket.domain.Stage;
import vn.tram.ticket.repository.StageRepository;
import vn.tram.ticket.service.criteria.StageCriteria;

/**
 * Service for executing complex queries for {@link Stage} entities in the database.
 * The main input is a {@link StageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Stage} or a {@link Page} of {@link Stage} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StageQueryService extends QueryService<Stage> {

    private final Logger log = LoggerFactory.getLogger(StageQueryService.class);

    private final StageRepository stageRepository;

    public StageQueryService(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    /**
     * Return a {@link List} of {@link Stage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Stage> findByCriteria(StageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stage> specification = createSpecification(criteria);
        return stageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Stage} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Stage> findByCriteria(StageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stage> specification = createSpecification(criteria);
        return stageRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stage> specification = createSpecification(criteria);
        return stageRepository.count(specification);
    }

    /**
     * Function to convert {@link StageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Stage> createSpecification(StageCriteria criteria) {
        Specification<Stage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Stage_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Stage_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Stage_.location));
            }
            if (criteria.getSeatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSeatId(), root -> root.join(Stage_.seats, JoinType.LEFT).get(Seat_.id))
                    );
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(Stage_.events, JoinType.LEFT).get(Event_.id))
                    );
            }
        }
        return specification;
    }
}
