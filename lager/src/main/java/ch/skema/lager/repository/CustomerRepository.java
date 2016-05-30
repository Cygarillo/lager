package ch.skema.lager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.skema.lager.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByLastNameStartsWithIgnoreCase(String lastName);
}
