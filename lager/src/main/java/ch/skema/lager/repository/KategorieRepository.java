package ch.skema.lager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.Kategorie;

public interface KategorieRepository extends JpaRepository<Kategorie, Long> {

	List<Kategorie> findByNameStartsWithIgnoreCase(String name);
}
