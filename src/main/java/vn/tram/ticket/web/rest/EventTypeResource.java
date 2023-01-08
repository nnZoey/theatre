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
import vn.tram.ticket.domain.EventType;
import vn.tram.ticket.repository.EventTypeRepository;
import vn.tram.ticket.service.EventTypeQueryService;
import vn.tram.ticket.service.EventTypeService;
import vn.tram.ticket.service.criteria.EventTypeCriteria;
import vn.tram.ticket.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.tram.ticket.domain.EventType}.
 */
@RestController
@RequestMapping("/api")
public class EventTypeResource {

    private final Logger log = LoggerFactory.getLogger(EventTypeResource.class);

    private static final String ENTITY_NAME = "eventType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventTypeService eventTypeService;

    private final EventTypeRepository eventTypeRepository;

    private final EventTypeQueryService eventTypeQueryService;

    public EventTypeResource(
        EventTypeService eventTypeService,
        EventTypeRepository eventTypeRepository,
        EventTypeQueryService eventTypeQueryService
    ) {
        this.eventTypeService = eventTypeService;
        this.eventTypeRepository = eventTypeRepository;
        this.eventTypeQueryService = eventTypeQueryService;
    }

    /**
     * {@code POST  /event-types} : Create a new eventType.
     *
     * @param eventType the eventType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventType, or with status {@code 400 (Bad Request)} if the eventType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/event-types")
    public ResponseEntity<EventType> createEventType(@RequestBody EventType eventType) throws URISyntaxException {
        log.debug("REST request to save EventType : {}", eventType);
        if (eventType.getId() != null) {
            throw new BadRequestAlertException("A new eventType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventType result = eventTypeService.save(eventType);
        return ResponseEntity
            .created(new URI("/api/event-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-types/:id} : Updates an existing eventType.
     *
     * @param id the id of the eventType to save.
     * @param eventType the eventType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventType,
     * or with status {@code 400 (Bad Request)} if the eventType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/event-types/{id}")
    public ResponseEntity<EventType> updateEventType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventType eventType
    ) throws URISyntaxException {
        log.debug("REST request to update EventType : {}, {}", id, eventType);
        if (eventType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventType result = eventTypeService.update(eventType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-types/:id} : Partial updates given fields of an existing eventType, field will ignore if it is null
     *
     * @param id the id of the eventType to save.
     * @param eventType the eventType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventType,
     * or with status {@code 400 (Bad Request)} if the eventType is not valid,
     * or with status {@code 404 (Not Found)} if the eventType is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/event-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventType> partialUpdateEventType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventType eventType
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventType partially : {}, {}", id, eventType);
        if (eventType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventType> result = eventTypeService.partialUpdate(eventType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eventType.getId().toString())
        );
    }

    /**
     * {@code GET  /event-types} : get all the eventTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventTypes in body.
     */
    @GetMapping("/event-types")
    public ResponseEntity<List<EventType>> getAllEventTypes(
        EventTypeCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get EventTypes by criteria: {}", criteria);
        Page<EventType> page = eventTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-types/count} : count all the eventTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/event-types/count")
    public ResponseEntity<Long> countEventTypes(EventTypeCriteria criteria) {
        log.debug("REST request to count EventTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-types/:id} : get the "id" eventType.
     *
     * @param id the id of the eventType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/event-types/{id}")
    public ResponseEntity<EventType> getEventType(@PathVariable Long id) {
        log.debug("REST request to get EventType : {}", id);
        Optional<EventType> eventType = eventTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventType);
    }

    /**
     * {@code DELETE  /event-types/:id} : delete the "id" eventType.
     *
     * @param id the id of the eventType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/event-types/{id}")
    public ResponseEntity<Void> deleteEventType(@PathVariable Long id) {
        log.debug("REST request to delete EventType : {}", id);
        eventTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
