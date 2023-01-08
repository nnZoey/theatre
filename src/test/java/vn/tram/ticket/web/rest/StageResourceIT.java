package vn.tram.ticket.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.tram.ticket.IntegrationTest;
import vn.tram.ticket.domain.Event;
import vn.tram.ticket.domain.Seat;
import vn.tram.ticket.domain.Stage;
import vn.tram.ticket.repository.StageRepository;
import vn.tram.ticket.service.criteria.StageCriteria;

/**
 * Integration tests for the {@link StageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StageResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/stages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStageMockMvc;

    private Stage stage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stage createEntity(EntityManager em) {
        Stage stage = new Stage().name(DEFAULT_NAME).location(DEFAULT_LOCATION);
        return stage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stage createUpdatedEntity(EntityManager em) {
        Stage stage = new Stage().name(UPDATED_NAME).location(UPDATED_LOCATION);
        return stage;
    }

    @BeforeEach
    public void initTest() {
        stage = createEntity(em);
    }

    @Test
    @Transactional
    void createStage() throws Exception {
        int databaseSizeBeforeCreate = stageRepository.findAll().size();
        // Create the Stage
        restStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stage)))
            .andExpect(status().isCreated());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeCreate + 1);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStage.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createStageWithExistingId() throws Exception {
        // Create the Stage with an existing ID
        stage.setId(1L);

        int databaseSizeBeforeCreate = stageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stage)))
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStages() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList
        restStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get the stage
        restStageMockMvc
            .perform(get(ENTITY_API_URL_ID, stage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getStagesByIdFiltering() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        Long id = stage.getId();

        defaultStageShouldBeFound("id.equals=" + id);
        defaultStageShouldNotBeFound("id.notEquals=" + id);

        defaultStageShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStageShouldNotBeFound("id.greaterThan=" + id);

        defaultStageShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStageShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllStagesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where name equals to DEFAULT_NAME
        defaultStageShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the stageList where name equals to UPDATED_NAME
        defaultStageShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStagesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where name in DEFAULT_NAME or UPDATED_NAME
        defaultStageShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the stageList where name equals to UPDATED_NAME
        defaultStageShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStagesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where name is not null
        defaultStageShouldBeFound("name.specified=true");

        // Get all the stageList where name is null
        defaultStageShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllStagesByNameContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where name contains DEFAULT_NAME
        defaultStageShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the stageList where name contains UPDATED_NAME
        defaultStageShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStagesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where name does not contain DEFAULT_NAME
        defaultStageShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the stageList where name does not contain UPDATED_NAME
        defaultStageShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllStagesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where location equals to DEFAULT_LOCATION
        defaultStageShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the stageList where location equals to UPDATED_LOCATION
        defaultStageShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllStagesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultStageShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the stageList where location equals to UPDATED_LOCATION
        defaultStageShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllStagesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where location is not null
        defaultStageShouldBeFound("location.specified=true");

        // Get all the stageList where location is null
        defaultStageShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllStagesByLocationContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where location contains DEFAULT_LOCATION
        defaultStageShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the stageList where location contains UPDATED_LOCATION
        defaultStageShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllStagesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        // Get all the stageList where location does not contain DEFAULT_LOCATION
        defaultStageShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the stageList where location does not contain UPDATED_LOCATION
        defaultStageShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllStagesBySeatIsEqualToSomething() throws Exception {
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            stageRepository.saveAndFlush(stage);
            seat = SeatResourceIT.createEntity(em);
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        em.persist(seat);
        em.flush();
        stage.addSeat(seat);
        stageRepository.saveAndFlush(stage);
        Long seatId = seat.getId();

        // Get all the stageList where seat equals to seatId
        defaultStageShouldBeFound("seatId.equals=" + seatId);

        // Get all the stageList where seat equals to (seatId + 1)
        defaultStageShouldNotBeFound("seatId.equals=" + (seatId + 1));
    }

    @Test
    @Transactional
    void getAllStagesByEventIsEqualToSomething() throws Exception {
        Event event;
        if (TestUtil.findAll(em, Event.class).isEmpty()) {
            stageRepository.saveAndFlush(stage);
            event = EventResourceIT.createEntity(em);
        } else {
            event = TestUtil.findAll(em, Event.class).get(0);
        }
        em.persist(event);
        em.flush();
        stage.addEvent(event);
        stageRepository.saveAndFlush(stage);
        Long eventId = event.getId();

        // Get all the stageList where event equals to eventId
        defaultStageShouldBeFound("eventId.equals=" + eventId);

        // Get all the stageList where event equals to (eventId + 1)
        defaultStageShouldNotBeFound("eventId.equals=" + (eventId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStageShouldBeFound(String filter) throws Exception {
        restStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStageShouldNotBeFound(String filter) throws Exception {
        restStageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingStage() throws Exception {
        // Get the stage
        restStageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Update the stage
        Stage updatedStage = stageRepository.findById(stage.getId()).get();
        // Disconnect from session so that the updates on updatedStage are not directly saved in db
        em.detach(updatedStage);
        updatedStage.name(UPDATED_NAME).location(UPDATED_LOCATION);

        restStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStage))
            )
            .andExpect(status().isOk());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStage.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();
        stage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();
        stage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();
        stage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStageWithPatch() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Update the stage using partial update
        Stage partialUpdatedStage = new Stage();
        partialUpdatedStage.setId(stage.getId());

        partialUpdatedStage.name(UPDATED_NAME);

        restStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStage))
            )
            .andExpect(status().isOk());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStage.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateStageWithPatch() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        int databaseSizeBeforeUpdate = stageRepository.findAll().size();

        // Update the stage using partial update
        Stage partialUpdatedStage = new Stage();
        partialUpdatedStage.setId(stage.getId());

        partialUpdatedStage.name(UPDATED_NAME).location(UPDATED_LOCATION);

        restStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStage))
            )
            .andExpect(status().isOk());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
        Stage testStage = stageList.get(stageList.size() - 1);
        assertThat(testStage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStage.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();
        stage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();
        stage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stage))
            )
            .andExpect(status().isBadRequest());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStage() throws Exception {
        int databaseSizeBeforeUpdate = stageRepository.findAll().size();
        stage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Stage in the database
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStage() throws Exception {
        // Initialize the database
        stageRepository.saveAndFlush(stage);

        int databaseSizeBeforeDelete = stageRepository.findAll().size();

        // Delete the stage
        restStageMockMvc
            .perform(delete(ENTITY_API_URL_ID, stage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stage> stageList = stageRepository.findAll();
        assertThat(stageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
