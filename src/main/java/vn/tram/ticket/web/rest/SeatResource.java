package vn.tram.ticket.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.tram.ticket.domain.Seat;
import vn.tram.ticket.repository.SeatRepository;
import vn.tram.ticket.service.SeatQueryService;
import vn.tram.ticket.service.SeatService;
import vn.tram.ticket.service.criteria.SeatCriteria;
import vn.tram.ticket.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.tram.ticket.domain.Seat}.
 */
@RestController
@RequestMapping("/api")
public class SeatResource {

    private final Logger log = LoggerFactory.getLogger(SeatResource.class);

    private static final String ENTITY_NAME = "seat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeatService seatService;

    private final SeatRepository seatRepository;

    private final SeatQueryService seatQueryService;

    public SeatResource(SeatService seatService, SeatRepository seatRepository, SeatQueryService seatQueryService) {
        this.seatService = seatService;
        this.seatRepository = seatRepository;
        this.seatQueryService = seatQueryService;
    }

    /**
     * {@code POST  /seats} : Create a new seat.
     *
     * @param seat the seat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seat, or with status {@code 400 (Bad Request)} if the seat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seats")
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) throws URISyntaxException {
        log.debug("REST request to save Seat : {}", seat);
        if (seat.getId() != null) {
            throw new BadRequestAlertException("A new seat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Seat result = seatService.save(seat);
        return ResponseEntity
            .created(new URI("/api/seats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seats/:id} : Updates an existing seat.
     *
     * @param id the id of the seat to save.
     * @param seat the seat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seat,
     * or with status {@code 400 (Bad Request)} if the seat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seats/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Seat seat)
        throws URISyntaxException {
        log.debug("REST request to update Seat : {}, {}", id, seat);
        if (seat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Seat result = seatService.update(seat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seats/:id} : Partial updates given fields of an existing seat, field will ignore if it is null
     *
     * @param id the id of the seat to save.
     * @param seat the seat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seat,
     * or with status {@code 400 (Bad Request)} if the seat is not valid,
     * or with status {@code 404 (Not Found)} if the seat is not found,
     * or with status {@code 500 (Internal Server Error)} if the seat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/seats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Seat> partialUpdateSeat(@PathVariable(value = "id", required = false) final Long id, @RequestBody Seat seat)
        throws URISyntaxException {
        log.debug("REST request to partial update Seat partially : {}, {}", id, seat);
        if (seat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, seat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Seat> result = seatService.partialUpdate(seat);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, seat.getId().toString())
        );
    }

    /**
     * {@code GET  /seats} : get all the seats.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seats in body.
     */
    @GetMapping("/seats")
    public ResponseEntity<List<Seat>> getAllSeats(SeatCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Seats by criteria: {}", criteria);
        Page<Seat> page = seatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seats/count} : count all the seats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/seats/count")
    public ResponseEntity<Long> countSeats(SeatCriteria criteria) {
        log.debug("REST request to count Seats by criteria: {}", criteria);
        return ResponseEntity.ok().body(seatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seats/:id} : get the "id" seat.
     *
     * @param id the id of the seat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seats/{id}")
    public ResponseEntity<Seat> getSeat(@PathVariable Long id) {
        log.debug("REST request to get Seat : {}", id);
        Optional<Seat> seat = seatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seat);
    }

    /**
     * {@code DELETE  /seats/:id} : delete the "id" seat.
     *
     * @param id the id of the seat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seats/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        log.debug("REST request to delete Seat : {}", id);
        seatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
