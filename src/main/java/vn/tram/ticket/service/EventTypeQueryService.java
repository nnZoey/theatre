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
import vn.tram.ticket.domain.EventType;
import vn.tram.ticket.repository.EventTypeRepository;
import vn.tram.ticket.service.criteria.EventTypeCriteria;

/**
 * Service for executing complex queries for {@link EventType} entities in the database.
 * The main input is a {@link EventTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EventType} or a {@link Page} of {@link EventType} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTypeQueryService extends QueryService<EventType> {

    private final Logger log = LoggerFactory.getLogger(EventTypeQueryService.class);

    private final EventTypeRepository eventTypeRepository;

    public EventTypeQueryService(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    /**
     * Return a {@link List} of {@link EventType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EventType> findByCriteria(EventTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EventType> specification = createSpecification(criteria);
        return eventTypeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EventType} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventType> findByCriteria(EventTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventType> specification = createSpecification(criteria);
        return eventTypeRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventType> specification = createSpecification(criteria);
        return eventTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventType> createSpecification(EventTypeCriteria criteria) {
        Specification<EventType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EventType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EventType_.name));
            }
            if (criteria.getEventId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getEventId(), root -> root.join(EventType_.event, JoinType.LEFT).get(Event_.id))
                    );
            }
        }
        return specification;
    }
}
