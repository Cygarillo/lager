package ch.skema.lager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.Kunde;

public interface KundeRepository extends JpaRepository<Kunde, Long> {

	List<Kunde> findByNameStartsWithIgnoreCase(String name);
}
