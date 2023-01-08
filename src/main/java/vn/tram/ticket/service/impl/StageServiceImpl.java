package vn.tram.ticket.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.tram.ticket.domain.Stage;
import vn.tram.ticket.repository.StageRepository;
import vn.tram.ticket.service.StageService;

/**
 * Service Implementation for managing {@link Stage}.
 */
@Service
@Transactional
public class StageServiceImpl implements StageService {

    private final Logger log = LoggerFactory.getLogger(StageServiceImpl.class);

    private final StageRepository stageRepository;

    public StageServiceImpl(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    @Override
    public Stage save(Stage stage) {
        log.debug("Request to save Stage : {}", stage);
        return stageRepository.save(stage);
    }

    @Override
    public Stage update(Stage stage) {
        log.debug("Request to update Stage : {}", stage);
        return stageRepository.save(stage);
    }

    @Override
    public Optional<Stage> partialUpdate(Stage stage) {
        log.debug("Request to partially update Stage : {}", stage);

        return stageRepository
            .findById(stage.getId())
            .map(existingStage -> {
                if (stage.getName() != null) {
                    existingStage.setName(stage.getName());
                }
                if (stage.getLocation() != null) {
                    existingStage.setLocation(stage.getLocation());
                }

                return existingStage;
            })
            .map(stageRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Stage> findAll(Pageable pageable) {
        log.debug("Request to get all Stages");
        return stageRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Stage> findOne(Long id) {
        log.debug("Request to get Stage : {}", id);
        return stageRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stage : {}", id);
        stageRepository.deleteById(id);
    }
}
