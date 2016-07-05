package ch.skema.lager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.Bestellung;

public interface BestellungRepositroy extends JpaRepository<Bestellung, Long> {

}
