package vn.tram.ticket.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.tram.ticket.domain.EventType;
import vn.tram.ticket.repository.EventTypeRepository;
import vn.tram.ticket.service.EventTypeService;

/**
 * Service Implementation for managing {@link EventType}.
 */
@Service
@Transactional
public class EventTypeServiceImpl implements EventTypeService {

    private final Logger log = LoggerFactory.getLogger(EventTypeServiceImpl.class);

    private final EventTypeRepository eventTypeRepository;

    public EventTypeServiceImpl(EventTypeRepository eventTypeRepository) {
        this.eventTypeRepository = eventTypeRepository;
    }

    @Override
    public EventType save(EventType eventType) {
        log.debug("Request to save EventType : {}", eventType);
        return eventTypeRepository.save(eventType);
    }

    @Override
    public EventType update(EventType eventType) {
        log.debug("Request to update EventType : {}", eventType);
        return eventTypeRepository.save(eventType);
    }

    @Override
    public Optional<EventType> partialUpdate(EventType eventType) {
        log.debug("Request to partially update EventType : {}", eventType);

        return eventTypeRepository
            .findById(eventType.getId())
            .map(existingEventType -> {
                if (eventType.getName() != null) {
                    existingEventType.setName(eventType.getName());
                }

                return existingEventType;
            })
            .map(eventTypeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventType> findAll(Pageable pageable) {
        log.debug("Request to get all EventTypes");
        return eventTypeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EventType> findOne(Long id) {
        log.debug("Request to get EventType : {}", id);
        return eventTypeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete EventType : {}", id);
        eventTypeRepository.deleteById(id);
    }
}
