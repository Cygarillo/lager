package ch.skema.lager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.Bestellung;

public interface BestellungRepository extends JpaRepository<Bestellung, Long> {

}
