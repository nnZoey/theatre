package vn.tram.ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.tram.ticket.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
