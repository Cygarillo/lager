package ch.skema.lager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.Produkt;

public interface ProduktRepository extends JpaRepository<Produkt, Long> {

	List<Produkt> findByNameStartsWithIgnoreCase(String name);
}
