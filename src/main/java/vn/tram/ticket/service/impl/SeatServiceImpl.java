package vn.tram.ticket.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.tram.ticket.domain.Seat;
import vn.tram.ticket.repository.SeatRepository;
import vn.tram.ticket.service.SeatService;

/**
 * Service Implementation for managing {@link Seat}.
 */
@Service
@Transactional
public class SeatServiceImpl implements SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatServiceImpl.class);

    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @Override
    public Seat save(Seat seat) {
        log.debug("Request to save Seat : {}", seat);
        return seatRepository.save(seat);
    }

    @Override
    public Seat update(Seat seat) {
        log.debug("Request to update Seat : {}", seat);
        return seatRepository.save(seat);
    }

    @Override
    public Optional<Seat> partialUpdate(Seat seat) {
        log.debug("Request to partially update Seat : {}", seat);

        return seatRepository
            .findById(seat.getId())
            .map(existingSeat -> {
                if (seat.getRow() != null) {
                    existingSeat.setRow(seat.getRow());
                }
                if (seat.getCol() != null) {
                    existingSeat.setCol(seat.getCol());
                }
                if (seat.getSeatClass() != null) {
                    existingSeat.setSeatClass(seat.getSeatClass());
                }

                return existingSeat;
            })
            .map(seatRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Seat> findAll(Pageable pageable) {
        log.debug("Request to get all Seats");
        return seatRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Seat> findOne(Long id) {
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Seat : {}", id);
        seatRepository.deleteById(id);
    }
}
