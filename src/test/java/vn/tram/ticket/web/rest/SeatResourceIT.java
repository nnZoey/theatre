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
import vn.tram.ticket.domain.Seat;
import vn.tram.ticket.domain.Stage;
import vn.tram.ticket.domain.Ticket;
import vn.tram.ticket.repository.SeatRepository;
import vn.tram.ticket.service.criteria.SeatCriteria;

/**
 * Integration tests for the {@link SeatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatResourceIT {

    private static final String DEFAULT_ROW = "AAAAAAAAAA";
    private static final String UPDATED_ROW = "BBBBBBBBBB";

    private static final Long DEFAULT_COL = 1L;
    private static final Long UPDATED_COL = 2L;
    private static final Long SMALLER_COL = 1L - 1L;

    private static final String DEFAULT_SEAT_CLASS = "AAAAAAAAAA";
    private static final String UPDATED_SEAT_CLASS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatMockMvc;

    private Seat seat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createEntity(EntityManager em) {
        Seat seat = new Seat().row(DEFAULT_ROW).col(DEFAULT_COL).seatClass(DEFAULT_SEAT_CLASS);
        return seat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createUpdatedEntity(EntityManager em) {
        Seat seat = new Seat().row(UPDATED_ROW).col(UPDATED_COL).seatClass(UPDATED_SEAT_CLASS);
        return seat;
    }

    @BeforeEach
    public void initTest() {
        seat = createEntity(em);
    }

    @Test
    @Transactional
    void createSeat() throws Exception {
        int databaseSizeBeforeCreate = seatRepository.findAll().size();
        // Create the Seat
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isCreated());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate + 1);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getRow()).isEqualTo(DEFAULT_ROW);
        assertThat(testSeat.getCol()).isEqualTo(DEFAULT_COL);
        assertThat(testSeat.getSeatClass()).isEqualTo(DEFAULT_SEAT_CLASS);
    }

    @Test
    @Transactional
    void createSeatWithExistingId() throws Exception {
        // Create the Seat with an existing ID
        seat.setId(1L);

        int databaseSizeBeforeCreate = seatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeats() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seat.getId().intValue())))
            .andExpect(jsonPath("$.[*].row").value(hasItem(DEFAULT_ROW)))
            .andExpect(jsonPath("$.[*].col").value(hasItem(DEFAULT_COL.intValue())))
            .andExpect(jsonPath("$.[*].seatClass").value(hasItem(DEFAULT_SEAT_CLASS)));
    }

    @Test
    @Transactional
    void getSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get the seat
        restSeatMockMvc
            .perform(get(ENTITY_API_URL_ID, seat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seat.getId().intValue()))
            .andExpect(jsonPath("$.row").value(DEFAULT_ROW))
            .andExpect(jsonPath("$.col").value(DEFAULT_COL.intValue()))
            .andExpect(jsonPath("$.seatClass").value(DEFAULT_SEAT_CLASS));
    }

    @Test
    @Transactional
    void getSeatsByIdFiltering() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        Long id = seat.getId();

        defaultSeatShouldBeFound("id.equals=" + id);
        defaultSeatShouldNotBeFound("id.notEquals=" + id);

        defaultSeatShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSeatShouldNotBeFound("id.greaterThan=" + id);

        defaultSeatShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSeatShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSeatsByRowIsEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where row equals to DEFAULT_ROW
        defaultSeatShouldBeFound("row.equals=" + DEFAULT_ROW);

        // Get all the seatList where row equals to UPDATED_ROW
        defaultSeatShouldNotBeFound("row.equals=" + UPDATED_ROW);
    }

    @Test
    @Transactional
    void getAllSeatsByRowIsInShouldWork() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where row in DEFAULT_ROW or UPDATED_ROW
        defaultSeatShouldBeFound("row.in=" + DEFAULT_ROW + "," + UPDATED_ROW);

        // Get all the seatList where row equals to UPDATED_ROW
        defaultSeatShouldNotBeFound("row.in=" + UPDATED_ROW);
    }

    @Test
    @Transactional
    void getAllSeatsByRowIsNullOrNotNull() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where row is not null
        defaultSeatShouldBeFound("row.specified=true");

        // Get all the seatList where row is null
        defaultSeatShouldNotBeFound("row.specified=false");
    }

    @Test
    @Transactional
    void getAllSeatsByRowContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where row contains DEFAULT_ROW
        defaultSeatShouldBeFound("row.contains=" + DEFAULT_ROW);

        // Get all the seatList where row contains UPDATED_ROW
        defaultSeatShouldNotBeFound("row.contains=" + UPDATED_ROW);
    }

    @Test
    @Transactional
    void getAllSeatsByRowNotContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where row does not contain DEFAULT_ROW
        defaultSeatShouldNotBeFound("row.doesNotContain=" + DEFAULT_ROW);

        // Get all the seatList where row does not contain UPDATED_ROW
        defaultSeatShouldBeFound("row.doesNotContain=" + UPDATED_ROW);
    }

    @Test
    @Transactional
    void getAllSeatsByColIsEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col equals to DEFAULT_COL
        defaultSeatShouldBeFound("col.equals=" + DEFAULT_COL);

        // Get all the seatList where col equals to UPDATED_COL
        defaultSeatShouldNotBeFound("col.equals=" + UPDATED_COL);
    }

    @Test
    @Transactional
    void getAllSeatsByColIsInShouldWork() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col in DEFAULT_COL or UPDATED_COL
        defaultSeatShouldBeFound("col.in=" + DEFAULT_COL + "," + UPDATED_COL);

        // Get all the seatList where col equals to UPDATED_COL
        defaultSeatShouldNotBeFound("col.in=" + UPDATED_COL);
    }

    @Test
    @Transactional
    void getAllSeatsByColIsNullOrNotNull() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col is not null
        defaultSeatShouldBeFound("col.specified=true");

        // Get all the seatList where col is null
        defaultSeatShouldNotBeFound("col.specified=false");
    }

    @Test
    @Transactional
    void getAllSeatsByColIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col is greater than or equal to DEFAULT_COL
        defaultSeatShouldBeFound("col.greaterThanOrEqual=" + DEFAULT_COL);

        // Get all the seatList where col is greater than or equal to UPDATED_COL
        defaultSeatShouldNotBeFound("col.greaterThanOrEqual=" + UPDATED_COL);
    }

    @Test
    @Transactional
    void getAllSeatsByColIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col is less than or equal to DEFAULT_COL
        defaultSeatShouldBeFound("col.lessThanOrEqual=" + DEFAULT_COL);

        // Get all the seatList where col is less than or equal to SMALLER_COL
        defaultSeatShouldNotBeFound("col.lessThanOrEqual=" + SMALLER_COL);
    }

    @Test
    @Transactional
    void getAllSeatsByColIsLessThanSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col is less than DEFAULT_COL
        defaultSeatShouldNotBeFound("col.lessThan=" + DEFAULT_COL);

        // Get all the seatList where col is less than UPDATED_COL
        defaultSeatShouldBeFound("col.lessThan=" + UPDATED_COL);
    }

    @Test
    @Transactional
    void getAllSeatsByColIsGreaterThanSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where col is greater than DEFAULT_COL
        defaultSeatShouldNotBeFound("col.greaterThan=" + DEFAULT_COL);

        // Get all the seatList where col is greater than SMALLER_COL
        defaultSeatShouldBeFound("col.greaterThan=" + SMALLER_COL);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatClassIsEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatClass equals to DEFAULT_SEAT_CLASS
        defaultSeatShouldBeFound("seatClass.equals=" + DEFAULT_SEAT_CLASS);

        // Get all the seatList where seatClass equals to UPDATED_SEAT_CLASS
        defaultSeatShouldNotBeFound("seatClass.equals=" + UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatClassIsInShouldWork() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatClass in DEFAULT_SEAT_CLASS or UPDATED_SEAT_CLASS
        defaultSeatShouldBeFound("seatClass.in=" + DEFAULT_SEAT_CLASS + "," + UPDATED_SEAT_CLASS);

        // Get all the seatList where seatClass equals to UPDATED_SEAT_CLASS
        defaultSeatShouldNotBeFound("seatClass.in=" + UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatClass is not null
        defaultSeatShouldBeFound("seatClass.specified=true");

        // Get all the seatList where seatClass is null
        defaultSeatShouldNotBeFound("seatClass.specified=false");
    }

    @Test
    @Transactional
    void getAllSeatsBySeatClassContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatClass contains DEFAULT_SEAT_CLASS
        defaultSeatShouldBeFound("seatClass.contains=" + DEFAULT_SEAT_CLASS);

        // Get all the seatList where seatClass contains UPDATED_SEAT_CLASS
        defaultSeatShouldNotBeFound("seatClass.contains=" + UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatClassNotContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatClass does not contain DEFAULT_SEAT_CLASS
        defaultSeatShouldNotBeFound("seatClass.doesNotContain=" + DEFAULT_SEAT_CLASS);

        // Get all the seatList where seatClass does not contain UPDATED_SEAT_CLASS
        defaultSeatShouldBeFound("seatClass.doesNotContain=" + UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void getAllSeatsByTicketIsEqualToSomething() throws Exception {
        Ticket ticket;
        if (TestUtil.findAll(em, Ticket.class).isEmpty()) {
            seatRepository.saveAndFlush(seat);
            ticket = TicketResourceIT.createEntity(em);
        } else {
            ticket = TestUtil.findAll(em, Ticket.class).get(0);
        }
        em.persist(ticket);
        em.flush();
        seat.setTicket(ticket);
        ticket.setSeat(seat);
        seatRepository.saveAndFlush(seat);
        Long ticketId = ticket.getId();

        // Get all the seatList where ticket equals to ticketId
        defaultSeatShouldBeFound("ticketId.equals=" + ticketId);

        // Get all the seatList where ticket equals to (ticketId + 1)
        defaultSeatShouldNotBeFound("ticketId.equals=" + (ticketId + 1));
    }

    @Test
    @Transactional
    void getAllSeatsByStageIsEqualToSomething() throws Exception {
        Stage stage;
        if (TestUtil.findAll(em, Stage.class).isEmpty()) {
            seatRepository.saveAndFlush(seat);
            stage = StageResourceIT.createEntity(em);
        } else {
            stage = TestUtil.findAll(em, Stage.class).get(0);
        }
        em.persist(stage);
        em.flush();
        seat.setStage(stage);
        seatRepository.saveAndFlush(seat);
        Long stageId = stage.getId();

        // Get all the seatList where stage equals to stageId
        defaultSeatShouldBeFound("stageId.equals=" + stageId);

        // Get all the seatList where stage equals to (stageId + 1)
        defaultSeatShouldNotBeFound("stageId.equals=" + (stageId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSeatShouldBeFound(String filter) throws Exception {
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seat.getId().intValue())))
            .andExpect(jsonPath("$.[*].row").value(hasItem(DEFAULT_ROW)))
            .andExpect(jsonPath("$.[*].col").value(hasItem(DEFAULT_COL.intValue())))
            .andExpect(jsonPath("$.[*].seatClass").value(hasItem(DEFAULT_SEAT_CLASS)));

        // Check, that the count call also returns 1
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSeatShouldNotBeFound(String filter) throws Exception {
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeat() throws Exception {
        // Get the seat
        restSeatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat
        Seat updatedSeat = seatRepository.findById(seat.getId()).get();
        // Disconnect from session so that the updates on updatedSeat are not directly saved in db
        em.detach(updatedSeat);
        updatedSeat.row(UPDATED_ROW).col(UPDATED_COL).seatClass(UPDATED_SEAT_CLASS);

        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getRow()).isEqualTo(UPDATED_ROW);
        assertThat(testSeat.getCol()).isEqualTo(UPDATED_COL);
        assertThat(testSeat.getSeatClass()).isEqualTo(UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void putNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setId(seat.getId());

        partialUpdatedSeat.row(UPDATED_ROW).col(UPDATED_COL).seatClass(UPDATED_SEAT_CLASS);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getRow()).isEqualTo(UPDATED_ROW);
        assertThat(testSeat.getCol()).isEqualTo(UPDATED_COL);
        assertThat(testSeat.getSeatClass()).isEqualTo(UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void fullUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setId(seat.getId());

        partialUpdatedSeat.row(UPDATED_ROW).col(UPDATED_COL).seatClass(UPDATED_SEAT_CLASS);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getRow()).isEqualTo(UPDATED_ROW);
        assertThat(testSeat.getCol()).isEqualTo(UPDATED_COL);
        assertThat(testSeat.getSeatClass()).isEqualTo(UPDATED_SEAT_CLASS);
    }

    @Test
    @Transactional
    void patchNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeDelete = seatRepository.findAll().size();

        // Delete the seat
        restSeatMockMvc
            .perform(delete(ENTITY_API_URL_ID, seat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
