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
import vn.tram.ticket.domain.Seat;
import vn.tram.ticket.repository.SeatRepository;
import vn.tram.ticket.service.criteria.SeatCriteria;

/**
 * Service for executing complex queries for {@link Seat} entities in the database.
 * The main input is a {@link SeatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Seat} or a {@link Page} of {@link Seat} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SeatQueryService extends QueryService<Seat> {

    private final Logger log = LoggerFactory.getLogger(SeatQueryService.class);

    private final SeatRepository seatRepository;

    public SeatQueryService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    /**
     * Return a {@link List} of {@link Seat} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Seat> findByCriteria(SeatCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Seat> specification = createSpecification(criteria);
        return seatRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Seat} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Seat> findByCriteria(SeatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Seat> specification = createSpecification(criteria);
        return seatRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SeatCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Seat> specification = createSpecification(criteria);
        return seatRepository.count(specification);
    }

    /**
     * Function to convert {@link SeatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Seat> createSpecification(SeatCriteria criteria) {
        Specification<Seat> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Seat_.id));
            }
            if (criteria.getRow() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRow(), Seat_.row));
            }
            if (criteria.getCol() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCol(), Seat_.col));
            }
            if (criteria.getSeatClass() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeatClass(), Seat_.seatClass));
            }
            if (criteria.getTicketId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTicketId(), root -> root.join(Seat_.tickets, JoinType.LEFT).get(Ticket_.id))
                    );
            }
            if (criteria.getStageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getStageId(), root -> root.join(Seat_.stage, JoinType.LEFT).get(Stage_.id))
                    );
            }
        }
        return specification;
    }
}
