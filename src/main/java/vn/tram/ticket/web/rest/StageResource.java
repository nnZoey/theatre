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
import vn.tram.ticket.domain.Stage;
import vn.tram.ticket.repository.StageRepository;
import vn.tram.ticket.service.StageQueryService;
import vn.tram.ticket.service.StageService;
import vn.tram.ticket.service.criteria.StageCriteria;
import vn.tram.ticket.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link vn.tram.ticket.domain.Stage}.
 */
@RestController
@RequestMapping("/api")
public class StageResource {

    private final Logger log = LoggerFactory.getLogger(StageResource.class);

    private static final String ENTITY_NAME = "stage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StageService stageService;

    private final StageRepository stageRepository;

    private final StageQueryService stageQueryService;

    public StageResource(StageService stageService, StageRepository stageRepository, StageQueryService stageQueryService) {
        this.stageService = stageService;
        this.stageRepository = stageRepository;
        this.stageQueryService = stageQueryService;
    }

    /**
     * {@code POST  /stages} : Create a new stage.
     *
     * @param stage the stage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stage, or with status {@code 400 (Bad Request)} if the stage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stages")
    public ResponseEntity<Stage> createStage(@RequestBody Stage stage) throws URISyntaxException {
        log.debug("REST request to save Stage : {}", stage);
        if (stage.getId() != null) {
            throw new BadRequestAlertException("A new stage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stage result = stageService.save(stage);
        return ResponseEntity
            .created(new URI("/api/stages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stages/:id} : Updates an existing stage.
     *
     * @param id the id of the stage to save.
     * @param stage the stage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stage,
     * or with status {@code 400 (Bad Request)} if the stage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stages/{id}")
    public ResponseEntity<Stage> updateStage(@PathVariable(value = "id", required = false) final Long id, @RequestBody Stage stage)
        throws URISyntaxException {
        log.debug("REST request to update Stage : {}, {}", id, stage);
        if (stage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Stage result = stageService.update(stage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stages/:id} : Partial updates given fields of an existing stage, field will ignore if it is null
     *
     * @param id the id of the stage to save.
     * @param stage the stage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stage,
     * or with status {@code 400 (Bad Request)} if the stage is not valid,
     * or with status {@code 404 (Not Found)} if the stage is not found,
     * or with status {@code 500 (Internal Server Error)} if the stage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Stage> partialUpdateStage(@PathVariable(value = "id", required = false) final Long id, @RequestBody Stage stage)
        throws URISyntaxException {
        log.debug("REST request to partial update Stage partially : {}, {}", id, stage);
        if (stage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Stage> result = stageService.partialUpdate(stage);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, stage.getId().toString())
        );
    }

    /**
     * {@code GET  /stages} : get all the stages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stages in body.
     */
    @GetMapping("/stages")
    public ResponseEntity<List<Stage>> getAllStages(
        StageCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Stages by criteria: {}", criteria);
        Page<Stage> page = stageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /stages/count} : count all the stages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/stages/count")
    public ResponseEntity<Long> countStages(StageCriteria criteria) {
        log.debug("REST request to count Stages by criteria: {}", criteria);
        return ResponseEntity.ok().body(stageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /stages/:id} : get the "id" stage.
     *
     * @param id the id of the stage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stages/{id}")
    public ResponseEntity<Stage> getStage(@PathVariable Long id) {
        log.debug("REST request to get Stage : {}", id);
        Optional<Stage> stage = stageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stage);
    }

    /**
     * {@code DELETE  /stages/:id} : delete the "id" stage.
     *
     * @param id the id of the stage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stages/{id}")
    public ResponseEntity<Void> deleteStage(@PathVariable Long id) {
        log.debug("REST request to delete Stage : {}", id);
        stageService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
