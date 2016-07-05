package ch.skema.lager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.BestellPosition;

public interface BestellPositionRepository extends JpaRepository<BestellPosition, Long> {

}
