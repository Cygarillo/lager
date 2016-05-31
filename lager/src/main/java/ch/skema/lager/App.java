package ch.skema.lager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ch.skema.lager.domain.Kunde;
import ch.skema.lager.repository.KundeRepository;

@SpringBootApplication
public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class);
		new BrowserLauncher().launch();
	}

	@Bean
	public CommandLineRunner loadData(KundeRepository repository) {
		return (args) -> {
			// save a couple of customers
			repository.save(new Kunde("Jack Bauer"));
			repository.save(new Kunde("Chloe O'Brian"));
			repository.save(new Kunde("Kim Bauer"));
			repository.save(new Kunde("David Palmer"));
			repository.save(new Kunde("Michelle Dessler"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Kunde customer : repository.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Kunde customer = repository.findOne(1L);
			log.info("Customer found with findOne(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastNameStartsWithIgnoreCase('Bauer'):");
			log.info("--------------------------------------------");
			for (Kunde bauer : repository.findByNameStartsWithIgnoreCase("Bauer")) {
				log.info(bauer.toString());
			}
			log.info("");
		};
	}
}
